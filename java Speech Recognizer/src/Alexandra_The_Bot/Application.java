package Alexandra_The_Bot;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

import marytts.TextToSpeech;
import marytts.signalproc.effects.JetPilotEffect;
import net.sourceforge.javaflacencoder.FLACFileWriter;

/**
 * This is where all begins .
 * 
 * @author Bharat
 *
 */
public class Application {
	
	private final TextToSpeech tts = new TextToSpeech();
	private final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private final GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	String oldText = "";
	
	/**
	 * Constructor
	 */
	public Application() {
		
		//Duplex Configuration
		duplex.setLanguage("en");
		
		duplex.addResponseListener(new GSpeechResponseListener() {
			
			public void onResponse(GoogleResponse googleResponse) {
				String output = "";
				
				//Get the response from Google Cloud
				output = googleResponse.getResponse();
				System.out.println(output);
				if (output != null) {
					makeDecision(output);
				} else
					System.out.println("Output was null");
			}
		});
		
		//---------------MaryTTS Configuration-----------------------------
		
		// Setting the Current Voice
		tts.setVoice("cmu-slt-hsmm");
		
		//JetPilotEffect
		JetPilotEffect jetPilotEffect = new JetPilotEffect(); //epic fun!!!
		jetPilotEffect.setParams("amount:100");
		
		//Apply the effects
		//tts.getMarytts().setAudioEffects(jetPilotEffect.getFullEffectAsString());// + "+" + stadiumEffect.getFullEffectAsString());
		
		//Start the Speech Recognition
		startSpeechRecognition();
		
	}
	
	/**
	 * This method makes a decision based on the given text of the Speech Recognition
	 * 
	 * @param text
	 */
	public void makeDecision(String output) {
		output = output.trim();
		//System.out.println(output.trim());
		
		//We don't want duplicate responses
		if (!oldText.equals(output))
			oldText = output;
		else
			return;
		
		if (output.contains("hello")) {//Hello
			speak("Hello");
			
		} else if (output.contains("introduce yourself")) {//Introduce your self		
			speak("My name is Bharat , i am Papa");
			
		} else if (output.contains("oh boy") || output.contains("obey")) {//obey
			speak("Baap Hu");
			
		} else if (output.contains("what is your profession")) {//what is your profession	
			speak("I am a Software Engineer");
			
		} else if (output.contains("do you have any girlfriend")) { //do you have any girlfriend	
			speak("Yes");
			
		} else if (output.contains("where do you live")) {//where do you live	
			speak("I am living in london");
			
		} else if (output.contains("I think you're funny") || output.contains("I think you are funny")) {//I think you are funny		
			speak("Yeah you too!");
			
		} else if (output.contains("Are You Hrony")) {//I think you are Horny		
			speak("Dont make me horny ");
			
		} else if (output.contains("you want sex")) {//damm		
			speak("Shut up");
		} else if (output.contains("Do want to do bussiness")) {	
			speak("Offcourse");
			
		} else if (output.contains("who's your daddy") || output.contains("but I am the boss")|| output.contains("Who's your daddy")) {
			speak("Fuck off");
			
		} else if (output.contains("show me some respect")) {	
			speak("Ok i will try");
			
		} else if (output.contains("tell me a story")) {
			speak("I dont know any story");
			
		} else if (output.contains("why do you speak like that")) {
			speak("Like what?");
			
		} else if (output.contains("say hi to jennifer")) {
			speak("Hello Miss jennifer? Kasi Hoo");
			
		} else if (output.contains("stop speech recognition")) {
			stopSpeechRecognition();
			
		} else { //Nothing matches here?
			System.out.println("Not entered on any else if statement");
		}
		
	}
	
	/**
	 * Calls the MaryTTS to say the given text
	 * 
	 * @param text
	 */
	public void speak(String text) {
		System.out.println(text);
		//Check if it is already speaking
		if (!tts.isSpeaking())
			new Thread(() -> tts.speak(text, 2.0f, true, false)).start();
		
	}
	
	/**
	 * Starts the Speech Recognition
	 */
	public void startSpeechRecognition() {
		//Start a new Thread so our application don't lags
		new Thread(() -> {
			try {
				duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
			} catch (LineUnavailableException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	/**
	 * Stops the Speech Recognition
	 */
	public void stopSpeechRecognition() {
		mic.close();
		System.out.println("Stopping Speech Recognition...." + " , Microphone State is:" + mic.getState());
	}
	
	public static void main(String[] args) {
		new Application();
	}
	
}
