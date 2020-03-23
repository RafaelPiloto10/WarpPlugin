# Minecraft: Warp Plugin
A custom plugin for teleports

## Installation:
 * Go to the [releases](https://github.com/RafaelPiloto10/WarpPlugin/releases) tab and download the latest release
 * Upload to your server
 * Create directory `WarpData/warps.dat` in your server root directory: `/`
 
## Permissions:
* `warpplugin.use` - Can the user use `/warp`
* `warpplugin.set_world` - Can the user set world warps

## Usage:
* `/warp <name>` - Warp to a set location
* `/warp set <name>` - Create a custom player-specific named warp
* `/warp set:world <name>` - Create a custom named world warp (anyone can use this)
* `/warp remove <name>` - Remove a named warp
* `/warp list` - List the warps accessible by the player
* `/warp help` - To learn more about each command

### Credits:
Created by [Rafael Piloto](https://rafaelpiloto10.herokuapp.com/)

## TODO:
  [x] Cannot list warps - Works with multiple warps, remove trailing comma
  [x] Error when trying to warp into non-existent warp
  [x] No XP Message
  [x] Fix message for player warp set
  [x] No cap on warp set, warp set sometimes gives no message
  * Must have world warp set before can custom warp ? 
  