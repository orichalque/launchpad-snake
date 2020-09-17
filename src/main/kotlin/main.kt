
import net.thecodersbreakfast.lp4j.midi.*
import net.thecodersbreakfast.lp4j.api.*


fun main(args: Array<String>) {

    /**
     * net.thecodersbreakfast.lp4j.midi.MidiDeviceConfiguration relies on 'Launchpad S' midi device
     * You might have to change according to the launchpad you use e.g., 'Launpad Mini' ...
     */
    val launchpad = MidiLaunchpad(MidiDeviceConfiguration.autodetect())

    /**
     * 500 corresponds to the ms duration between each frame. Change according to your needs.
     */
    Snake(500, launchpad).play()

    

}

