package main.me.therom.core;

public interface Logger
{
	default void log(String log)
	{
		System.out.println(log);
	}

	default void log(Exception ex)
	{
		System.out.println(ex.toString());
	}
}
