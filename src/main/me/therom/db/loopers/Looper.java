package main.me.therom.db.loopers;

import java.io.File;
import java.io.IOException;

public abstract class Looper
{
	protected final File file;

	public Looper(File file)
	{
		this.file = file;
	}

	public abstract int getLength();

	public abstract boolean hasNext();

	public abstract Object getNextElement() throws IOException;
}
