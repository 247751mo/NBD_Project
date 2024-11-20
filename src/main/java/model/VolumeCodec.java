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

        // Write the type discriminator based on the class
        if (volume instanceof Book) {
            writer.writeString("_type", "book");
        } else if (volume instanceof Monthly) {
            writer.writeString("_type", "monthly");
        } else if (volume instanceof Publication) {
            writer.writeString("_type", "publication");
        }

        // Write common fields for all Volume types
        writer.writeString("_id", volume.getVolumeId());
        writer.writeString("title", volume.getTitle());
        writer.writeString("genre", volume.getGenre());

        // Write subclass-specific fields
        if (volume instanceof Book book) {
            writer.writeString("author", book.getAuthor());
        } else if (volume instanceof Monthly monthly) {
            writer.writeString("editor", monthly.getPublisher());
        } else if (volume instanceof Publication publication) {
            writer.writeString("publisher", publication.getPublisher());
        }

        writer.writeEndDocument();
    }

    @Override
    public Volume decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String type = null;
        String id = null;
        String title = null;
        String genre = null;
        boolean isAvailable = false;
        String author = null;
        int pageCount = 0;
        String editor = null;
        String publicationMonth = null;
        String publisher = null;
        int issueNumber = 0;

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            switch (fieldName) {
                case "_type":
                    type = reader.readString();
                    break;
                case "_id":
                    id = reader.readString();
                    break;
                case "title":
                    title = reader.readString();
                    break;
                case "genre":
                    genre = reader.readString();
                    break;
                case "isAvailable":
                    isAvailable = reader.readBoolean();
                    break;
                case "author":
                    author = reader.readString();
                    break;
                case "pageCount":
                    pageCount = reader.readInt32();
                    break;
                case "editor":
                    editor = reader.readString();
                    break;
                case "publicationMonth":
                    publicationMonth = reader.readString();
                    break;
                case "publisher":
                    publisher = reader.readString();
                    break;
                case "issueNumber":
                    issueNumber = reader.readInt32();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.readEndDocument();

        // Instantiate the correct subclass based on the "_type" field
        return switch (type) {
            case "book" -> new Book(id, title, genre, author);
            case "monthly" -> new Monthly(id, title, genre, publisher);
            case "publication" -> new Publication(id, title, genre, publisher);
            case null, default -> throw new IllegalArgumentException("Unsupported volume type: " + type);
        };
    }

    @Override
    public Class<Volume> getEncoderClass() {
        return Volume.class;
    }
}
