package com.github.cloudyrock.mongock.examples.codec;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class ZonedDateTimeCodec implements Codec<ZonedDateTime> {

  @Override
  public void encode(final BsonWriter writer, final ZonedDateTime value, final EncoderContext encoderContext) {
    writer.writeDateTime(value.toInstant().getEpochSecond() * 1_000);
  }

  @Override
  public ZonedDateTime decode(final BsonReader reader, final DecoderContext decoderContext) {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(reader.readDateTime() / 1_000), ZoneId.systemDefault());
  }

  @Override
  public Class<ZonedDateTime> getEncoderClass() {
    return ZonedDateTime.class;
  }
}