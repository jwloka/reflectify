# reflectify

## “Reflective Access at Your Finger Tips”
Accessing APIs via java.lang.relfect can be tedious and quite painful. Reflectify is a small [Eclipse](http://eclipse.org/jdt) plug-in that offers Quick Assists for "reflectifying" class, instance, method and field accesses. The quick assists are enabled if an expression or a statement is selected in the Java editor.

## Supported Transformations
The current version provides the following transformations as auto-complete:

**Reflectify Class Access**
``` java
Foo.class → Class.forName("p.Foo")
```

**Reflectify Class Access**
``` java
Foo.class → Class.forName("p.Foo")
```

**Reflectify Instance Access**
``` java
new Foo() → Class.forName("p.Foo").newInstance()
```

**Reflectify Method Access**
``` java
v.getFoo() → v.getClass().getMethod("getFoo").invoke(v)
```

**Reflectify Field Access**
``` java
v.bar → v.getClass().getField("bar").get(v)
```

**Reflectify Field Assignment**
``` java
v.bar = new Object() → v.getClass().getField("bar").set(v, new Object())
```

## Download
Reflectify is available under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html "Eclipse Public License") (EPL) and tested with Eclipse 3.4.x and above. It is available either for download or as update site:
- JAR file [ [org.wloka.reflectify.jar](http://wloka.org/eclipse/reflectify/plugins/org.wloka.reflectify_0.0.2.201308251403.jar) ]
- Update site [ [http://wloka.org/eclipse/reflectify/](http://wloka.org/eclipse/reflectify/) ]


