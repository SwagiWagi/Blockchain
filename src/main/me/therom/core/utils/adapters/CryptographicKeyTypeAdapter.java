package main.me.therom.core.utils.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.me.therom.cryptography.HashHelper;

import java.io.IOException;
import java.security.PublicKey;

public class CryptographicKeyTypeAdapter extends TypeAdapter<PublicKey>
{
    @Override
    public void write(JsonWriter out, PublicKey value) throws IOException
    {
        out.value(new String(HashHelper.encodePublicKey(value)));
    }

    @Override
    public PublicKey read(JsonReader reader)
    {
        try {
            return HashHelper.decodePublicKey(reader.nextString().getBytes());
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
