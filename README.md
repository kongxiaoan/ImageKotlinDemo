> 依赖注入是面向对象编程的一种设计模式，其目的是为了降低程序耦合，这个耦合就是类之间的依赖引起的。

- 符合单一指责原则以及开闭原则

> 什么是依赖注入

类通常需要引用其他类，
eg: Car 类可能需要People 类，这些类称为依赖项，Car 依赖于People 才能运行

> 以前在Android开发中用到的依赖注入不是很多，知道最近看了几个项目包括写了几周Java项目，才注意到以来注入的方式在现有技术中的地位，在Java Spring Boot 中几乎将依赖注入用到了极致，减少了太多的工作量


> 优势：

- 重用代码
- 易于重构
- 易于测试


### 一、注入方式

1. 通过接口注入

```java
public interface ClassBInterface {
    void setClassB(ClassB classB);
}

class ClassB {
    public void doSomething() {

    }
}

class ClassA implements ClassBInterface {
    ClassB classB;

    @Override
    public void setClassB(ClassB classB) {
        this.classB = classB;
    }
}
```
2. 通过set方法注入
3. 通过构造方法注入
4. 通过Java 注解注入

### 二、 Android中的依赖注入

> 一般情况下，Android 中主要用构造函数注入或者set 方法注入

对于Android来讲，Dagger2 无非是现在最好的依赖注入框架，Google亲自操刀，静态编译期完成注入，对于性能不受影响，有利于维护，能减少由于对象引用而造成的OOM等问题。

#### 2.1 Dagger2 

> DI(dependency injection) ，分三部分：

- 依赖提供方
- 依赖需求方
- 依赖注入（桥梁）

解释一下什么叫依赖

> 一个类有两个变量，这两个变量就是他的依赖，初始化依赖两种方法，自己初始化，外部初始化就叫依赖注入。

我们要使用一个组件一定是 **先了解它提供了什么？其次是和我们业务相关联的需求是什么？最后是我们怎么用它**

在Dagger 中
- 一般将@Moudle 注解作为依赖提供方
- @Component 作为依赖之间连接的桥梁
- @Inject 注解的变量作为需求方（也可以用于依赖的提供方）

#### 2.1.1 使用

> 在Android 项目的build.gradle中添加

```groovy
apply plugin: 'kotlin-kapt'
// ...


implementation 'com.google.dagger:dagger:2.11'
    kapt 'com.google.dagger:dagger-compiler:2.11'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.11'
    //java注解
    implementation 'org.glassfish:javax.annotation:10.0-b28'
```

#### @Inject

> 一般用于注入属性、方法、构造方法，项目中注入构造方法的使用方式居多，有两个功能

- 作为依赖的提供方

```kotlin
// 注解构造方法
class Navigator @Inject constructor() {
    fun navigator() {
        println("navigator method")
    }
}
```
- 作为依赖的需求方

```kotlin
class MainActivity : AppCompatActivity() {
// 将Navigator 注入到MainActivty 中，使得MainActivity 具有navigator的引用
    @Inject lateinit var navigator: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator.navigator()
    }
}
```

两个@Inject 注解形成了依赖关系，
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020090214360524.png#pic_center)

what?? 不是说@Inject既然事依赖的提供方也是依赖的需求方吗？ 难道我使用错了？

别着急，哈哈 我们不是说依赖关系中还有@Component 是需求方与提供方之间的桥梁。

#### @Component

注解interface 

> 可以说是Dagger2 容器，是注入依赖和提供依赖之间的桥梁，把提供的依赖注入到所需要注入的依赖中

1. 申明一个接口并用@Component注解

```kotlin
@Component
interface ApplicationComponent {
//提供一个用于注解的方法
    fun inject(application: Dagger2Application)
}
```
2. rebuild 项目，会生成一个名为DaggerApplicationComponent 的文件，并且实现了ApplicationComponent,很明显这就是Dagger为我们生成的，

```kotlin

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerApplicationComponent implements ApplicationComponent {
  private MembersInjector<MainActivity> mainActivityMembersInjector;

  private DaggerApplicationComponent(Builder builder) {
  //...
```
3. 在Application中申明，也可以说是初始化并将ApplicationComponent中注解的引用提供给全局书用

```kotlin
class Dagger2Application: Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .build()
    }
    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}
```
4. 在MainActivity中

```kotlin
class MainActivity : AppCompatActivity() {
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as Dagger2Application).appComponent
    }
    @Inject lateinit var navigator: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 这才是真正的注入
        appComponent.inject(this)
        navigator.navigator()
    }
}
```
别忘了 在AndroidMianfest.xml 中配置application
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200902143646198.png#pic_center)


**当然在一个项目中要用一个框架，我们需要考虑能不能尽可能多的覆盖业务场景，那眼下就有一个问题，Dagger给第三方库提供注解**

#### @Module 和 @Provides

> @Module 和@Provides结合为Dagger2提供依赖关系，对上文@Inject第三点的补充，用于不能用@Inject提供依赖的地方，如第三方库提供的类，基本数据类型等不能修改源码的情况。@Provides仅能注解方法，且方法所在类要有@Module注解。注解后的方法表示Dagger2能用该方法实例对象提供依赖。按照惯例，@Provides方法的命名以provide为前缀，方便阅读管理。

eg: 

1. 创建@Module
```kotlin
@Module
class ApplicationModule {
}

```

2. 和@Provides 组合提供依赖

```kotlin
// 这就简单的提供了Retrofit（第三方库）依赖
@Module
class ApplicationModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().build()
    }
}
```

经过@Inject的教训，我们首先想到的应该是怎么和DaggerApplicationComponent 这个接口关联起来

3. 使用@Component 关联ApplicationModule

```kotlin

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(application: Dagger2Application)
    fun inject(activity: MainActivity)
}
```

4. 在Application中添加一行代码

```kotlin
val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            // 是这行
            .applicationModule(ApplicationModule())
            .build()
    }
```


#### 总结

> 首先@Component注解包含了一个Module类，表示Dagger2可以从Module类查找依赖，Dagger2会自动查找Module类有@Provides注释的方法实例依赖，最后完成注入
---


这就完成了Dagger2 简单的注解，在项目中也可以进行注解的使用了，当然Dagger2 的注解还有很多,可以参考 ：下面链接的文章，

[Android |《看完不忘系列》之dagger
](https://juejin.im/post/6865659377957732359)

看了这么多，我的初衷是Dagger2 下更加简单，代码量少的网络请求框架，前几天总结的一套框架和朋友们聊了一下 觉得比较大，有些臃肿

### 三、kotlin coroutines + jetpack + retrofit + okHttp3 + dagger 网络请求


> 具体思路是将固定的部分放在dagger2中，然后尽可能减少可变的代码

很显然Retrofit 的初始化将是必须放在Dagger2的注解中的

#### 3.1 在@Module中 申明retrofit的依赖

```kotlin 
@Module
class ApplicationModule(private val application: Dagger2Application) {

    @Provides
    @Singleton fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gank.io/api/v2/data/category/Girl/type/Girl/")
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun createClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }
}

```

有一个 **@Singleton** 注解，字面意思时单例，但是根据Dagger2 的编译方式，@Singleton只是对大家有一个提示作用，因为每次编译都是先检测某个注解有没有被编译，如果有的话是不会再次去编译的，所以不可能出现不是统一对象的引用的

#### 3.1.1 错误情况处理

> 在应用中选择的网络请求框架应该结合自己的需求去选择，但是任何一个框架我们都需要处理异常情况以及其他意外的错误情况 

```kotlin
// 使用密封类 密封类类似于枚举，但是比枚举更加灵活，可以携带参数等优点
sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    abstract class FeatureFailure : Failure()
}

```

#### 3.1.2 返回值处理

> 当然也少不了对返回值的处理， 我们将返回值处理为两种状态，即成功和失败

```kotlin
sealed class Either<out L, out R> {

    data class Error<out L>(val a: L) : Either<L, Nothing>()

    data class Success<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Success<R>

    val isLeft get() = this is Error<L>

    fun <L> left(a: L) = Error(a)

    fun <R> right(b: R) = Success(b)

    fun fold(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when(this) {
            is Error -> fnL(a)
            is Success -> fnR(b)
        }
}
```



> 任何一个成体系的东西都不是有具体顺序的，因为打开冰箱把大象放进去是有多种方式的，但是目的都是把大象放进冰箱， 所以我们不需要担心怎么去构造出来一个框架，我们只是按照自己的思路将我们要的东西写出来，然后完善它。所以不要想这一步之后我该做什么

#### 3.2 声明API 以及service

```kotlin
interface ImageApi {
    @GET("page/{page}/count/{size}")
    fun images(@Path("page") page: Int, @Path("size") size: Int): Call<ImageEntry>
}
```

```kotlin
@Singleton
class ImageService @Inject constructor(retrofit: Retrofit) : ImageApi {
    private val imageApi by lazy { retrofit.create(ImageApi::class.java) }
    override fun images(page: Int, size: Int) = imageApi.images(page, size)
}
```

这就将Retrofit和 API连接起来了， 那接下来我们在哪里使用ImageService 呢？很明显这就是网络请求服务的类，

> 在项目中推荐将网络请求或者说是数据来源都建立一个仓库，以便于集中处理和数据缓存设计

#### 3.3 建立数据仓库 

```
interface ImageRepository {
    fun images(page: Int, size: Int): Either<Failure, ImageEntry>

    class NetWork @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val imageService: ImageService
    ) : ImageRepository {
        override fun images(page: Int, size: Int): Either<Failure, ImageEntry> {
            return when (networkHandler.isConnected) {
                true -> request(
                    imageService.images(page, size),
                    {
                        it
                    },
                    ImageEntry.empty()
                )
                false, null -> Either.Error(Failure.NetworkConnection)
            }
        }

        private fun <T, R> request(
            call: Call<T>,
            transform: (T) -> R,
            default: T
        ): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Success(transform((response.body() ?: default)))
                    false -> Either.Error(Failure.ServerError)
                }
            } catch (e: Throwable) {
                Either.Error(Failure.ServerError)
            }
        }
    }
}
```

> 经过上面的两部操作，我们的答题框架已经出来了，现在就是写怎么去执行一个网路请求和怎么将请求到的数据展示到页面

#### 3.4 创建一个请求用例


> 我们将请求直接归纳为一个用例，就是专门用于请求的类

```kotlin

abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    class None
}
```

里面我们用到了协 程，并且暴露出一个run(params) 方法，用来实现具体的请求

> 具体的接口请求方法

```kotlin
class GetImage @Inject constructor(private val imageRepository: ImageRepository) :
    UseCase<ImageEntry, GetImage.Params>() {
    override suspend fun run(params: Params): Either<Failure, ImageEntry> = imageRepository.images(params.page, params.size)

    data class Params(val page: Int, val size: Int)
}
```

#### 3.5 创建ViewModel

```kotlin
class ImageViewModel @Inject constructor(private val getImage: GetImage) : BaseViewModel() {
    private val _image: MutableLiveData<List<Image>> = MutableLiveData()
    val image: LiveData<List<Image>> = _image

    fun loadImage(page: Int, size: Int) = getImage(GetImage.Params(page, size)) {
        it.fold(::handleFailure, ::handleImageList)
    }

    private fun handleImageList(imageEntry: ImageEntry) {
        _image.value = imageEntry.toImage()
    }
}

```

> "::" kotlin 中的双冒号操作符， 表示把一个方法当作一个参数，传递到另一个方法中进行使用

参考项目： [Android-CleanArchitecture-Kotlin](https://github.com/android10/Android-CleanArchitecture-Kotlin) 

解读项目： https://github.com/kongxiaoan/ImageKotlinDemo

[Demo 下载](https://github.com/kongxiaoan/ImageKotlinDemo)

#### 四、 目的

> 对于老外的一些项目是特别优秀的，但是由于他们的全英文对于英文薄弱的人来说看起来不容易理解，我这就是将看到的这个项目进行了拆分，自己又实现了一遍，从中获取到的一些知识，还是非常值得的
