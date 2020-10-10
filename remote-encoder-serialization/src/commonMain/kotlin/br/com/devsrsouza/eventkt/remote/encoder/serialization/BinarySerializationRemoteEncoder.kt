package br.com.devsrsouza.eventkt.remote.encoder.serialization

import br.com.devsrsouza.eventkt.remote.ListenerTypeSet
import br.com.devsrsouza.eventkt.remote.RemoteEncoder
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

class BinarySerializationRemoteEncoder(
    val binaryFormat: BinaryFormat
) : RemoteEncoder<ByteArray> {

    @OptIn(InternalSerializationApi::class)
    override fun encode(
        any: Any,
        listenTypes: ListenerTypeSet
    ): ByteArray {
        val type = any::class

        val serializer = type.serializer() as KSerializer<Any>

        val message = BynaryEventMessage(
            serializer.descriptor.serialName,
            binaryFormat.encodeToByteArray(serializer, any)
        )

        return binaryFormat.encodeToByteArray(BynaryEventMessage.serializer(), message)
    }

    @OptIn(InternalSerializationApi::class)
    override fun decode(
        value: ByteArray,
        listenTypes: ListenerTypeSet
    ): Any {
        val message = binaryFormat.decodeFromByteArray(BynaryEventMessage.serializer(), value)

        val serializer = getSerializer(message.type, listenTypes)

        return binaryFormat.decodeFromByteArray(serializer, message.content)
    }

}