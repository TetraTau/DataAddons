# DataAddons
## This library is an open-source component of TabLight project "Base-API"
### Have a question or want to discuss this library? Join [our discord server](https://discord.gg/upTtNyvkNf)
DataAddons is a library (or framework?) created for Minecraft providing comfortable abstractions making additions over already existing data, generally, **it is anti-pattern** ans **YOU SHOULDN'T USE IT** in normal programms. \
So why is it needed? It is needed for loading, storing, registering and processing of *data addons* and composing them out of different "sources" of data.
### Downloading
For now this library has no public repository artifact, so the only way you can use it is via jar.
```kotlin
dependencies {
    compileOnly(files("DataAddons-vX.X.jar"))
}
```
Later We'll publish an artifact to a public repository.

### Main concepts
Let's call types which have some data along with Minecraft's one *data addons*. These types have both *native* (Minecraft's) data and *custom* (plugin dev's) data.
Usually native data serializes when Minecraft server stops, so every developer who has ever made "customX" class makes the same pattern every time:
1. On server startup:
    * Execute a service looking for "native" data which has some tag distinguishes it from usual native data.
    * Execute service looking for "custom" data from its storage. (DB, serialized object, etc.)
    * Compose "native" and "custom" data into objects and "hold" these objects somewhere for further processing.
2. While server running:
    * Process "held" objects.
    * Make new "custom" data from "native" if needed.
3. On server shutdown:
    * Mark "custom" data generated from "native" while server running with "tag" which saves along with native data. (generally - NBT tag)
    * Save "custom" data to its storage. (DBs, serialization etc.)

As you probably noticed "these objects" which we construct, hold, process and store are *data addons*.

### Quick Start.
In your JavaPlugin class make bootstrap.
```java
public final class ExamplePlugin extends JavaPlugin {
    final DataAddonBootstrap bootstrap = new DataAddonBootstrap();
    
    @Override
    public void onEnable() {
        bootstrap.setContainer(GlobalGroupContainer.getInstance()); // 1.
        bootstrap.bootstrapRegistries("com.example.registries"); 
        bootstrap.bootstrapDataAddons("com.example.addons");
    }

    @Override
    public void onDisable() {
        logger.info("Disabling Tablight Entities plugin...");
    }
}
```
1. It uses static `GlobalGroupContainer`, so you can interact with other registered types of other users, \
For non-static `GroupContainer` write `new GroupContainer();`.

What is `com.example.registries`?
We call "registries" `TypeRegistry`, `TypeHolder` and `StoreLoadController` classes, what each registry do we'll discuss later.
In `com.example.registries` you should define classes as:
```java
@Registry("example-group")
public class ExampleTypeRegistry extends DefaultTypeRegistry {
}
```
```java
@Holder("example-group")
public class ExampleTypeHolder extends ConcurrentTypeHolder {
}
```
```java
@Controller("example-group")
public class ExampleController extends DefaultStoreLoadController {
}
```
Note that for proper connection these classes should have the same "group tag".
And You can override their methods to define your own behaviour for "pre-processing" and "post-processing".

What is `com.example.addons`? 
It is our data addons classes, we use them in "registries". You should define data addon class as:
```java
@DataAddon(
		identifier = "example", // 1.
		groupTag = "example-group", // 2.
		nativeClass = NativeExampleClass.class, // 3.
		lookup = ExampleDataAddonLookup.class
)
public class ExampleDataAddon {
	private String someString;
	private String someNativeStringData;

	public String getSomeString() {
		return someString;
	}

	public String getSomeNativeStringData() {
		return someNativeStringData;
	}

	public void setSomeNativeStringData(String someNativeStringData) {
		this.someNativeStringData = someNativeStringData;
	}

	@Store // 4
	public void store() {
		someString = "store";
	}

	@Load // 5
	public void load() {
		someString = "load";
	}
}
```
1. `identifier` is a unique ID in group in which this data addon registered.
2. `groupTag` is the same group tag as you registered "registries" before, 
it tells a bootstrap that this data addon processes in "registries" which have the same `groupTag` in their annotations.
3. `nativeClass` is a class which have "native" data.
4. `@Store` annotation identifies method which stores "custom" data to its storage.
5. `@Load` annotation is the same as `@Store` but loads "custom" data from its storage.

What is `lookup`?
`lookup` is a class which looks for a "native" data (in Minecraft server) marked with some tag, and instantiates data addon with **ONLY** with "native" data,
and puts this instance into a `TypeHolder`. \
Let's take a look at this example:
```java
public class NativeExample {
    private final String someNativeString;
    public NativeDummy(String nativeString) {
        this.someNativeString = nativeString;
    } 

    public String getSomeNativeString() {
        return someNativeString;
    }
}
```
```java
public class ExampleDataAddonLookup implements StoreLoadLookup<ExampleDataAddon, NativeExample> {

    private final Collection<NativeExample> nativeExamples = Lists.newArrayList( // we use a collection as an example, in plugins we use server data.
            new NativeExample("native1")
    );

    @Override
    public Supplier<Collection<ExampleDataAddon>> lookup() { // We return "lazy" Supplier returning collection instead of collection itself.
        return () -> nativeExamples.stream().map(nativeExamples -> {
            var dummy = new ExampleDataAddon();
            dummy.setSomeNativeStringData(nativeExamples.getSomeNativeString());
            return dummy;
        }).toList();
    }

    @Override
    public Stream<NativeExample> getNatives() { // We return "lazy" Stream instead of usual Collection.
        return nativeDummiesContainer.stream();
    }
}
```
I've written it all, how do I use it? \
Now, let's back to `DataAddonBootstrap`, it has everything configured, look at this example:
```java
public final class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        //...
        var typeReg = bootstrap.getRegistry(ExampleTypeRegistry.class);
        var typeHolder = bootstrap.getRegistry(ExampleTypeHolder.class);
        var controller = bootstrap.getRegistry(ExampleController.class);
        
        controller.lookupAndLoad(ExampleDataAddon.class); // it looks for this class using its lookup and loads "custom" data into "looked up" objects instances.
        Collection<ExampleDataAddon> heldExamples = typeHolder.getHeld(ExampleDataAddon.class); // You can obtain all configured data addons using holder.
        heldExamples.forEach(heldExample -> {
            System.out.println("Handling: " + heldExample.toString()); // And you handle them.
        });
        controller.store(ExampleDataAddon.class); // And you store it back.
    }
}
```
That's all! For further reading, read the java docs in files. This Library has more functional I didn't show in this quick start, 
such as LMAX Disruptor events in TypeHolder, or manual bootstrapping without annotations.

Contributing
----
Contributions are welcome! But you need to understand that your code will be used on TabLight's servers, so very slow or very hacky contributions will be denied.  
