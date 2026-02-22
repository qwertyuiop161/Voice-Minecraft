SPEECH TO TEXT SOFTWARE!!!!!!!!!!!

My friend broke his left arm, so I made this.

This service somewhat accurately (I dont have access to better models than vosk) writes whatever you say out loud when activated.
YOU MUST HAVE JAVA 25. NO OTHER TOOLS REQUIRED
NOT MEANT TO BE USED FOR NOR ACCURATE ENOUGH FOR PEOPLE WITH GENUINE DISABILITIES OR PARALYSIS. PLEASE FIND ANOTHER TOOL.
USAGE:

Non devs looking for a free tool:
1: download executable parts (the files that start with voicetotext)
2: see bottom for seperate windows and linux/mac instructions, come back here once done
3: wait for light to turn yellow
4: choose dictation mode for typing
5: click anywhere you want to write
6: click start recording
7: light green, click where you want to write again
8: begin talking slowly and clearly
9: pause for up to 7s after saying something.
10: Your text will appear pretty accurately
11: say backspace to delete words

Developers looking to mod and improve this tool
1: download project
2: Vosk model not included in project, to large to commit on my slow potato computer, so download one of your choice, but preferrably en-us-.22 (what it uses)
3: open IDE of choice
4: create project
5: import gradle project
6: select these files
7: add the downloaded extracted vosk model
8: open src/main/java
9: modify code as needed (remember to change model path if you use another model, such as the powerful but intensive .42-gigaspeech)
10: to run, open terminal and type .\gradlew run
11: when finished use compile tool to turn it into a standalone .exe

FOR NON DEVS WINDOWS VS BASH (linux/mac) SETUP
Windows
1: once parts are downloaded, put them in a standalone folder
2: open the folder and press shift+right click
3: select open powershell window here
3: run this command: cmd /c "copy /b VoiceToText-1.0.0.jar00.part+VoiceToText-1.0.0.jar01.part VoiceToText.jar"
5: open the folder with the part files again
6: you should now see a VoiceToText.jar that you can run with java 25
Linux/macOS
1: open downloads folder or wherever part files are
2: run "cat VoiceToText-1.0.0.jar*.part > VoiceToText-1.0.0.jar"
3: you should now see a VoiceToText.jar that you can run with java 25.

I Really only needed it to work somewhat well since my friend is getting his cast off in a couple weeks, but since this could be helpful for disabled people, if I get enough stars or forks on this project I might improve it using some cloud software or anything really.
If you do decide to fork and improve this, please give me credit for initial development.
