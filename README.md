# Snake4Launchpad 
Implementation of the awfully classic snake game to run on a Novation Launchpad

## Demo
![](https://github.com/orichalque/launchpad-snake/blob/gif/snake.gif?raw=true)

## Installation
Install https://github.com/OlivierCroisier/LP4J with Maven first, then:
`mvn clean package`

## Execution
`mvn exec:java`

## Troubleshooting
This program relies on https://github.com/OlivierCroisier/LP4J which was meant for the Launchpad S.
You might need to tweak it for a Launchpad Mini (this is only a string to change in the source of `net.thecodersbreakfast.lp4j.midiMidiDeviceConfiguration` )

