package model;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class VolumeCodec implements Codec<Volume> {

    @Override
    public void encode(BsonWriter writer, Volume volume, EncoderContext encoderContext) {
        writer.writeStartDocument();

        if (volume instanceof Book) {
            writer.writeString("_type", "book");
        } else if (volume instanceof Monthly) {
            writer.writeString("_type", "monthly");
        } else if (volume instanceof Publication) {
            writer.writeString("_type", "publication");
        }

        writer.writeInt32("_id", volume.getVolumeId());
        writer.writeString("title", volume.getTitle());
        writer.writeString("genre", volume.getGenre());
        writer.writeInt32("isRented", volume.getIsRented());
        writer.writeBoolean("isArchive", volume.isArchive());

        // Write subclass-specific fields
        if (volume instanceof Book book) {
            writer.writeString("author", book.getAuthor());
        } else if (volume instanceof Monthly monthly) {
            writer.writeString("publisher", monthly.getPublisher());
        } else if (volume instanceof Publication publication) {
            writer.writeString("publisher", publication.getPublisher());
        }

        writer.writeEndDocument();
    }

    @Override
    public Volume decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        // Variables to hold the field values
        String type = null;
        Integer id = null;
        String title = null;
        String genre = null;
        int isRented = 0;
        boolean isArchive = false;
        String author = null;
        String publisher = null;

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            switch (fieldName) {
                case "_type":
                    type = reader.readString();
                    break;
                case "_id":
                    id = reader.readInt32();
                    break;
                case "title":
                    title = reader.readString();
                    break;
                case "genre":
                    genre = reader.readString();
                    break;
                case "isRented":
                    isRented = reader.readInt32();
                    break;
                case "isArchive":
                    isArchive = reader.readBoolean();
                    break;
                case "author":
                    author = reader.readString();
                    break;
                case "publisher":
                    publisher = reader.readString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.readEndDocument();

        return switch (type) {
            case "book" -> new Book(id, title, genre, author);
            case "monthly" -> new Monthly(id, title, genre, publisher);
            case "weekly" -> new Weekly(id, title, genre, publisher);
            case "publication" -> new Publication(id, title, genre, publisher);
            default -> throw new IllegalArgumentException("Unsupported volume type: " + type);
        };
    }

    @Override
    public Class<Volume> getEncoderClass() {
        return Volume.class;
    }
}
