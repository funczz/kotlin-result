package io.kotlintest.provided

import java.io.*

interface ISerializableUtil {

     fun Any.dump(): ByteArray {
        return ByteArrayOutputStream().use { bo ->
            ObjectOutputStream(bo).use { oo ->
                oo.writeObject(this)
            }
            bo.toByteArray()
        }
    }

     fun ByteArray.load(): Any {
        return ByteArrayInputStream(this).use { bi ->
            ObjectInputStream(bi).use { oi ->
                oi.readObject()
            }
        }
    }

}

