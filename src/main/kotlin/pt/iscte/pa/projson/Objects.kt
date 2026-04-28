package pt.iscte.pa.projson

class Task(val description: String, val deadline:Date?,val dependencies: List<Task>)
data class Date(val day: Int, val month: Int, val year: Int)