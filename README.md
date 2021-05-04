# Android MVVM - Clean Architecture with DI 
MVVM with AAC ViewModel, StateHandler, LiveData, RX, Retrofit, Hilt, DiffUtil

## Implementation
### build.gradle (project)
```
// for hilt.
dependencies {
  classpath 'com.google.dagger:hilt-android-gradle-plugin:2.28-alpha'
}
```
### build.gradle (app)
```
dependencies {
    // fragment
    implementation "androidx.fragment:fragment-ktx:1.3.2"

    // rx
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'io.reactivex.rxjava2:rxkotlin:2.3.0'
    implementation 'com.jakewharton.rxbinding2:rxbinding-kotlin:2.1.1'
    implementation 'com.jakewharton.rxbinding2:rxbinding-appcompat-v7-kotlin:2.1.1'
    implementation 'com.jakewharton.rxbinding2:rxbinding-recyclerview-v7-kotlin:2.1.1'

    // retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.4.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.10.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'

    // hilt
    def dagger_version = "2.28-alpha"
    implementation "com.google.dagger:hilt-android:$dagger_version"
    kapt "com.google.dagger:hilt-android-compiler:$dagger_version"

    // lifecycle
    def lifecycle_version = "2.2.0"
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"

    def hilt_version = "1.0.0-alpha01"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:$hilt_version"
    kapt "androidx.hilt:hilt-compiler:$hilt_version"
}
```
## Repository
### Retrofit Api
```
@Module
@InstallIn(ApplicationComponent::class)
object Api {


    @Singleton
    @Provides
    @Named("base_url")
    fun create_base_url() = "https://api.github.com/"

    var token = ""

    @Singleton
    @Provides
    fun retrofit(@Named("base_url") baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).apply {

            val client = OkHttpClient.Builder().apply {

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(interceptor)

                addInterceptor(
                    Interceptor { chain ->
                        Log.d("Authorize", "intercepter ==================> ${token}")
                        val builder = chain.request().newBuilder()
                        val response = chain.proceed(builder.build())

                        val authorization = response.header("Authorization")
                        Log.d("Authorize", "Authorization=$authorization")

                        if (authorization?.isNotEmpty() == true) {
                            token = authorization
                        }
                        return@Interceptor response
                    }
                )
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(15, TimeUnit.SECONDS)
                writeTimeout(15, TimeUnit.SECONDS)
            }.build()

            client(client)
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create())

        }.build()
    }
}
```
### Base Util (DiffUtil)
```
open class DiffCallback<T: BaseDiffItem> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.getDiffId() == newItem.getDiffId()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.equals(newItem)
    }
}

interface BaseDiffItem {
    fun getDiffId(): String
}
```
### Remote Data Source (Retrofit)
```
@Module
@InstallIn(ApplicationComponent::class)
object GithubUserApi {

    @Provides
    fun githubUserService(retrofit: Retrofit): GithubUserService = retrofit.create(GithubUserService::class.java)
}

interface GithubUserService {
    @GET("users")
    fun getUsers(): Observable<List<GithubUser>>
}
```
### Local DAO and Entity (Room)
##### Dao
```
@Dao
interface GithubUserDao {
    @Query("SELECT * FROM github_user")
    suspend fun getAllUsers() : List<GithubUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<GithubUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: GithubUser)
}
```
##### Entity
```
@Entity(tableName = "github_user")
data class GithubUser (
        @PrimaryKey
        val id: Int,
        val avatar_url: String,
        val events_url: String,
        val followers_url: String,
        val following_url: String,
        val gists_url: String,
        val gravatar_id: String,
        val html_url: String,
        val login: String,
        val node_id: String,
        val organizations_url: String,
        val received_events_url: String,
        val repos_url: String,
        val site_admin: Boolean,
        val starred_url: String,
        val subscriptions_url: String,
        val type: String,
        val url: String,
) : BaseDiffItem {
        override fun getDiffId() = "$id"
}
```
### Repository
```
@Singleton
class GithubUserRepository @Inject constructor(
    private val remoteSource: GithubUserService,
    private val localDao: GithubUserDao
) {
    suspend fun getUsers() : Observable<List<GithubUser>> {

        return remoteSource.getUsers()
                .subscribeOn(Schedulers.io())
                .timeout(3, TimeUnit.SECONDS)
                .doOnEach { Log.e(javaClass.simpleName, "${it.error?.localizedMessage}") }
                .onErrorResumeNext (
                    Observable.just(localDao.getAllUsers())
                )
    }
}
```
## ViewModel
```
class GithubUserViewModel @Inject constructor(
    private val githubUserRepository : GithubUserRepository
): ViewModel(), LifecycleObserver {

    val userList by lazy { MutableLiveData<List<GithubUser>>() }

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            githubUserRepository.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        userList.postValue(response)
                    },{ error ->
                        Log.e("검색", "error=${error.localizedMessage}")
                    })
        }

    }

    fun onItemClick(user: GithubUser) {
        Log.d(javaClass.simpleName, "item clicked = $user")
    }
}
```
## View Composer
### Fragment (Dialog)
```
class UserDialog @Inject constructor(
        private val viewModel:GithubUserViewModel
): DialogFragment() {

    lateinit var binding: DialogUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@UserDialog
            vm = viewModel
            recyclerSchool.adapter = UserAdapter(viewModel)
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}
```
### Adapter
```
class UserAdapter(private val viewModel: GithubUserViewModel): ListAdapter<GithubUser, UserAdapter.Holder>(
    DiffCallback<GithubUser>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        val binding: ItemUserBinding = DataBindingUtil.bind(view)!!
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GithubUser) {
            binding.item = item
            binding.vm = viewModel
        }
    }
}

@BindingAdapter("bind_user_response")
fun bindRecyclerView(recyclerView: RecyclerView, item: List<GithubUser>?){
    item?.let { users ->
        val adapter = recyclerView.adapter as UserAdapter
        adapter.submitList(users)
    }
}
```
### View (layout)
```
  
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable name="vm" type="net.flow9.androidmvvm.viewmodel.GithubUserViewModel"/>
    </data>

    <LinearLayout
        android:layout_width="448dp"
        android:layout_height="560dp"
        android:layout_marginTop="24dp"
        android:layout_marginBottom="24dp"
        android:minHeight="560dp"
        android:background="#ffffff"
        android:orientation="vertical">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="55dp">

            <TextView
                android:id="@+id/labelSelectSchool"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="24dp"
                android:text="Github Users"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <ImageButton
                android:id="@+id/btnClose"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginRight="8dp"
                android:background="@android:color/transparent"
                android:padding="8dp"
                android:src="@drawable/ic_x_grey_19dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                 />

        </androidx.constraintlayout.widget.ConstraintLayout>

        <View
            android:id="@+id/divider"
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="#e8e8e8" />

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginStart="24dp"
            android:layout_marginEnd="24dp"
            android:layout_weight="1"
            android:orientation="vertical">

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recyclerSchool"
                bind_user_response="@{vm.userList}"
                android:layout_width="0dp"
                android:layout_height="0dp"
                app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                tools:listitem="@layout/item_user" />

        </androidx.constraintlayout.widget.ConstraintLayout>
    </LinearLayout>
</layout>
```
## Activity
```
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var userDialog:UserDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonOpen.setOnClickListener {
            userDialog.show(supportFragmentManager, "userDialog")
        }
    }
}
```
## Application
```
@HiltAndroidApp
class App : MultiDexApplication()
```