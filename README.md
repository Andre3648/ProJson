# ProJson

A Kotlin library for generating JSON with object reference support.
ProJson converts any Kotlin object into a JSON structure automatically,
making object reference management transparent to the client code.

---

## Gradle Configuration

Add the following to your `build.gradle.kts`:

```kotlin
dependencies {
    testImplementation(kotlin("test")) //it is already included by creating a kotlin project
    implementation(kotlin("reflect"))
}
```

---

## Data Model

ProJson is built around a sealed class hierarchy rooted at `JsonValue`:

- `JsonPrimitive` — represents a raw value: `String`, `Number`, `Boolean`, or `null`
- `JsonObject` — represents a JSON object with named properties
- `JsonArray` — represents an ordered list of JSON values
- `JsonReference` — represents a reference to a `JsonObject` via UUID

---

## Generating JSON with ProJson

The main entry point is `ProJson().toJson(obj)`, which converts any Kotlin object
into its corresponding `JsonValue`.

### Primitives

```kotlin
ProJson().toJson(null)    // JsonPrimitive(null)  → null
ProJson().toJson("Andre") // JsonPrimitive        → "Andr"
ProJson().toJson(28)      // JsonPrimitive        → 28
ProJson().toJson(true)    // JsonPrimitive        → true
```

### Collections

```kotlin
val json = ProJson().toJson(listOf("a", null, "b")) as JsonArray
println(json) // ["a", null, "b"]
```

### Maps

Maps are converted to `JsonObject` without a `$type` property:

```kotlin
val json = ProJson().toJson(mapOf("name" to "Andre", "age" to 28)) as JsonObject
println(json)
// {
//   "name": "Andre",
//   "age": 28
// }
```

### Kotlin Objects (via Reflection)

Any Kotlin object is converted to a `JsonObject` with a `$type` property
holding the class name. Properties are converted in declaration order.

```kotlin
data class Date(val day: Int, val month: Int, val year: Int)

val json = ProJson().toJson(Date(31, 4, 2026)) as JsonObject
println(json)
// {
//   "$type": "Date",
//   "day": 31,
//   "month": 4,
//   "year": 2026
// }
```

It is possible also use nested objects:

```kotlin
data class Date(val day: Int, val month: Int, val year: Int)
data class Task(val description: String, val deadline: Date?, val dependencies: List<Task>)

val json = ProJson().toJson(Task("T1", Date(30, 2, 2026), emptyList())) as JsonObject
println(json)
// {
//   "$type": "Task",
//   "description": "T1",
//   "deadline": {
//     "$type": "Date",
//     "day": 30,
//     "month": 2,
//     "year": 2026
//   },
//   "dependencies": []
// }
```

---

## JSON manipulation

### JsonObject

```kotlin
val json = JsonObject()

// Add or modify a property
json.setProperty("name", "Andre")
json.setProperty("age", 28)

// Read a property
val name = json.getProperty("name") // JsonPrimitive("Andre")

// Remove a property
json.removeProperty("age")

// Get all properties
val props = json.getProperties() // Map<String, JsonValue>
```

### JsonArray

```kotlin
val json = JsonArray()

// Add elements
json.add("a")
json.add(null)
json.add(42)

// Read an element by index
val first = json.get(0) // JsonPrimitive("a")

// Remove an element by index
json.remove(0)

// Get the number of elements
val count = json.size()

// Get all elements
val elements = json.getElements() // List<JsonValue>
```

---

## Formatted strings

All `JsonValue` types implement `toString()` to produce valid JSON text.

Arrays containing only primitive values are converted on a single line: (this for now has a bug)

```kotlin
val json = JsonArray()
json.add("a")
json.add(null)
json.add("b")
println(json) // ["a", null, "b"]
```

Arrays containing objects or nested arrays are pretty-printed:

```kotlin
val json = JsonArray()
val obj = JsonObject()
obj.setProperty("day", 31)
json.add(obj)
println(json)
// [
//   {
//     "day": 31
//   }
// ]
```

---

## Traversing the JSON Tree

Use `accept` to traverse the entire JSON tree depth-first.
The provided lambda is called for every node in the tree.

```kotlin
val json = ProJson().toJson(Date(31, 4, 2026))

// Print all nodes
json.accept { node ->
    println(node)
}

// Find all primitive values
val primitives = mutableListOf<JsonPrimitive>()
json.accept { node ->
    if (node is JsonPrimitive) primitives.add(node)
}

// Count all null values
var nullCount = 0
json.accept { node ->
    if (node is JsonPrimitive && node.value == null) nullCount++
}
```

---

## (Extra) Patterns used

ProJson is built around four well-known design patterns:

### Composite Pattern
The `JsonValue` sealed class hierarchy follows the Composite Pattern — `JsonObject` and `JsonArray`
are composite nodes that contain other `JsonValue` instances, while `JsonPrimitive` and `JsonReference`
are leaf nodes with no children. This mirrors the recursive tree structure of JSON itself.

```
JsonValue          ← Component (abstract)
├── JsonPrimitive  ← Leaf
├── JsonReference  ← Leaf
├── JsonObject     ← Composite (contains JsonValue properties)
└── JsonArray      ← Composite (contains JsonValue elements)
```

### Visitor Pattern
The `accept` extension function implements the Visitor Pattern using a lambda,
allowing the client to traverse the entire JSON tree without needing to know
how the structure works internally:

```kotlin
json.accept { node ->
    if (node is JsonPrimitive) println(node)
}
```

### Façade Pattern
`ProJson` is a Façade — it hides the complexity of reflection, JSON construction,
and recursive conversion behind a single method `toJson`. The client code does not
need to know about `KClass`, `primaryConstructor`, or `declaredMemberProperties`.
