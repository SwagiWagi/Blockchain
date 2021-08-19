package main.me.therom.core.utils.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.me.therom.cryptography.HashHelper;

import java.io.IOException;

public class SignatureTypeAdapter extends TypeAdapter<byte[]>
{
    @Override
    public void write(JsonWriter out, byte[] value) throws IOException
    {
        out.value(new String(HashHelper.encodeSignature(value)));
    }

    @Override
    public byte[] read(JsonReader reader)
    {
        try {
            return HashHelper.decodeSignature(reader.nextString().getBytes());
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
