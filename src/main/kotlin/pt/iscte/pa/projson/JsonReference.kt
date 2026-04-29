package pt.iscte.pa.projson

class JsonReference(val uuid: String) : JsonValue() {

    override fun toString(): String = $$"{ \"$ref\": \"$$uuid\" }"

}