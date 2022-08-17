# SkyHead
## General
![](https://img.shields.io/github/downloads/WispySparks/SkyHead/total?color=green&label=Downloads)

This is a 1.8.9 Minecraft Mod used to display Skywars (and Bedwars) Levels on Hypixel above player's head and in tab, to get started just put it in your mods folder and use the command
`/skyhead key <apikeyhere>` and it should start working. 
You can get your apikey on hypixel by doing the command, `/api`.
You can also abbreviate `/skyhead` to `/sh`. Turning the mod on and off will be remembered between launches through use of a config file. 
If it says "Limit" next to a players name that means you have hit the limit of requests for that minute and you will have to wait a little bit. 
This was built off of the recommended forge build for 1.8.9 This mod is use at your own risk, 
I am not responsible for any harm that may come to you or false bans that could potentially happen.
## Commands
- `/sh on` - Turns the mod on.
- `/sh off` - Turns the mod off.
- `/sh tab` - Toggles tab levels on and off.
- `/sh sw` - Changes displayed levels to Skywars.
- `/sh bw` - Changes displayed levels to Bedwars.
- `/sh requests` - Displays the number of requests made to the api this minute.
- `/sh size` - Displays the size of the player cache for the current mode you're in.
- `/sh key` - Used for setting your api key for the mod to use.
- `/sh help` - Displays a text block to explain every command.
## Installation
To install this mod simply grab the latest jar file from the releases tab on github and put it into your mods folder.
You will need forge 1.8.9 installed which you can download from [here](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html).
## Sidenote
Rapidly changing the mode of what level to display when name changes are happening due to games starting and such can lead to problems.
In general it should work but spamming the command can cause issues sometimes as players names/teams are changed. 
## Images
![2022-08-16_16 31 47](https://user-images.githubusercontent.com/101812473/184989339-faa9a65b-e894-40d6-b532-4ca70abd9bba.png)
![2022-08-16_16 33 01](https://user-images.githubusercontent.com/101812473/184989357-a69d7943-fbae-43c8-b1bb-7984da337177.png)
## Build Source
If you want to build the jar from source all you have to do is clone the repo and run ./gradlew build in the top directory of the mod. You will need JDK 8 for this to work.
