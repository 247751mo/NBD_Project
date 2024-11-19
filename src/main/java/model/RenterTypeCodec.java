package model;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.DecoderContext;

public class RenterTypeCodec implements Codec<RenterType> {

    @Override
    public void encode(BsonWriter writer, RenterType value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        if (value instanceof Card) {
            writer.writeString("_type", "Card");
        } else if (value instanceof NoCard) {
            writer.writeString("_type", "NoCard");
        } else {
            throw new IllegalArgumentException("Unknown RenterType: " + value.getClass());
        }
        writer.writeEndDocument();
    }

    @Override
    public RenterType decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();  // Start reading the document

        String type = null;

        // Iterate over the fields to extract the type
        while (reader.readName() != null) {  // Use readName() to read the field name
            String fieldName = reader.getCurrentName();
            if ("_type".equals(fieldName)) {
                type = reader.readString(); // Read the _type field
            } else {
                reader.skipValue(); // Skip other fields
            }
        }

        reader.readEndDocument();  // End reading the document

        // Return the appropriate instance based on the type
        switch (type) {
            case "Card":
                return new Card();
            case "NoCard":
                return new NoCard();
            default:
                throw new IllegalArgumentException("Unknown RenterType: " + type);
        }
    }

    @Override
    public Class<RenterType> getEncoderClass() {
        return RenterType.class;
    }
}
