SPEECH TO TEXT SOFTWARE!!!!!!!!!!!

My friend broke his left arm, so I made this.

This service somewhat accurately (I dont have access to better models than vosk) writes whatever you say out loud when activated.

USAGE:

Non devs looking for a free tool:
1: download and open executable
2: wait for light to turn yellow
3: choose dictation mode for typing
4: click anywhere you want to write
5: click start recording
6: light green, click where you want to write again
7: begin talking slowly and clearly
8: pause for up to 7s after saying something.
9: Your text will appear pretty accurately
10: say backspace to delete words

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

I Really only needed it to work somewhat well since my friend is getting his cast off in a couple weeks, but since this could be helpful for disabled people, if I get enough stars or forks on this project I might improve it using some cloud software or anything really.
If you do decide to fork and improve this, please give me credit for initial development.
