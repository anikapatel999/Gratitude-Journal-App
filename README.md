Gratitude Journal App
===

# Gratitude Journal

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [frames](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Gratitude journal is an app geared at improving users' mental health. Users can write daily gratitude journal entries and view them. They can also log their moods each day, connect with friends they're grateful for, and track their mental health progress over time.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:**
- **Mobile:**
- **Story:**
- **Market:**
- **Habit:**
- **Scope:**

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

*The user stories below are in the order that I'm planning to complete them, but I might move them around once I start working on the app. Also, I'm not sure about my time estimations - I'll probably also update those once I work more on it.*

* [x] User can create a new account
* [x] User can log in
* [x] User can log out
* [x] User can select an word that matches how their day went
    * [x] Users can swipe to the next screen to skip / continue?*
* [x] User can write an one entry each day and edit that entry at any time during that day
    * [x] Editing the entry / mood is prevented after 12am in the user's timezone (added 1 day to estimated time to do this)
* [x] User can view a calendar, which they can click on to see the entry from that day and scroll through the entries both forward and backwards
    * [x] implements infinite scroll (this will probably take most of the time)
* [x] User can view a quote that is generated based on the emoji mood
    * [x] user can swipe from FriendActivity to this activity and form this activity to HomeActivity (same as from MoodActivity to  ComposeActivity)
* [x] User can add other users as friends and close friends
    * [x] User can also remove them
    * [x] User can also view their friends/close friends list
* [x] User sees a notification icon when a friend mentions them in their gratitude journal entry (or the full post send as disappearing, non screenshottable messages, depending on the user's preference (close friends vs just friends))
    *  [x] User can turn on/off sending these notifications
    *  [x] User can turn on/off receiving these notifications
* [x] Use a library to add visual polish
    *  Calendar uses an extrenal library that makes it more customizable 


**Optional Nice-to-have Stories**
* [x] User can click on gifs from the home activity. Gifs are made using [Lottiefiles](https://lottiefiles.com/)
    * [x] clickable gifs are labeled
* [x] User can see the action bar with the correct buttons in each activity (for example, the home button is not visible in the home activity)
* [x] The background color of the app fits the app theme and helps reduce eye strain
* [x] The action bar color to fits the app theme
* [x] The back buttons work properly in every activity and are efficient (rather than making the code re-query somehting, they can pass it through an intent, for example)
* [x] User can "promote" a friend from friend to close friend without having to remove them to the close friends list first
    * acheived using a snackbar
* [ ] User receives push notifications near the end of the day if they have not written an entry for the day
* [x] User can see stats based on their mood selection
    * [x] in the form of a graph of moods over time so they can see progress / improvements
    * [x] in the form of a pi chart with moods
* [x] User can see stats based on their previous journal entries
    * in the form of a word cloud
* [ ] Composed entries are auto-saved even if the user does not select the save button
* [ ] User can scroll to more recent entries rather than pressing the clickable text to load more (infinite scroll upwards)
* [ ] User can add images into their journal entry
    *  [ ] User can add a background image to their entry
    *  [ ] The images can be placed anywhere in the entry
* [ ] Days on calendar are colored based on emoji selected
* [ ] App finds user's time zone based on their current location instead of having the user input it / change it in settings
* [ ] While the user writes their entry, the app plays music based on their emoji mood selection
    *  [ ] Music is randomized if the user skips the emoji selection page
    *  [ ] User can turn this off in the app's settings

### 2. Screen Archetypes

* Registration screen
    * User can create an account
* Settings screen
    * User can select their time zone
    * User can select their friend mentions settings (once stretch feature is implemented)
* Login screen
    * User can log in
* Home screen
    * User can select from 4 buttons:
        * Compose/edit entry
        * View calendar
        * View/edit friends list
        * View stats
    * As well as log out and view mentions from the action bar
* Emoji selection screen
    * User can select an emoji from 5(?) options. They are prompted to select the emoji that best matches their mood
    * User can select a skip option instead
    * Text next to / under the emoji clarifies what mood the emoji shows
* Quote screen
    * A quote is automatically displayed on the screen based on the emoji the user selected (or randomized if the user skips the emoji selection)
    * User can click the "next" button to leave this screen and enter the compose entry screen
* Compose entry screen
    * User can compose / edit their entry for the day.
    * There is a save button.
        * Can also save automatically (if stretch goal is acheived)
* Calendar view of past entries screen
    * User can view a calendar, which they can click on to see the entries from each day, as well as scroll through past/future entries from that day.
    * Days on calendar are colored based on emoji selected (if stretch goal is acheived)
* Scroll view of past entries screen
    * User can scroll through entries and see the emoji mood they selected that day
* View mentions screen
   * Users can select to view friend mentions or clise friend mentions
* View friend mentions screen
   * User can view a list of friends that mentioned them. This screen is non screenshotable, and mentions can only be viewed once
View close friend mentions screen
   * User can view a list of close friends that mentioned them. This screen is non screenshotable. If one of the entries is clicked on, the user is taken to the screen to view close friend entries.
View close friend entries screen
   * User can view the entries that the selected close friend mentioned them in. This screen is non screenshotable, and entries can only be viewed once

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* User can navigate to the home screen from anywhere
* User can log out from anywhere
* User can go to the settings screen from anywhere
* User can view mentiones from the home screen

**Flow Navigation** (Screen to Screen)

* Registration screen
    * --> settings screen
* Settings screen
    * --> home screen
* Login screen
    * --> home screen
* Home screen
    * --> compose entry screen
    * --> calendar view of past entries screen
    * --> friends screen
    * --> Cstats screen
* Emoji selection screen
    * --> home screen
    * swiping transitions to compose screen
* Compose entry screen
    * --> home screen (tab)
    * swiping transitions to mention friends screen
* Mention friends screen
   * --> swiping transitions to quote screen
* Quote screen
    * --> home screen
    * swiping transitions to compose entry screen
* Calendar view of past entries screen
    * --> home screen (tab)
    * --> scroll through past entries screen
* Scroll through past entries screen
    * none
* Friends screen
   * none
* Mentions screen
    * --> view friend mentions screen
    * --> view close friend mentions screen
* View friend mentions screen
    * none
* View close friend mentions screen
    * --> view close friend entries screen
* View close friend entries mentions screen
    * none

## Wireframes!
![](https://i.imgur.com/xV47yES.jpg)

### Models
**Parse:**

Users:
| Property  |  Type   | Description |
| ----- | --- | -------- |
| objectID |  String   | unique id for the user |
| username   |  String   | the user's username  |
| password   |  String   |the user's password|
| createdAt   |  String   | the date the user joined the app |
| friends  |  Array   | an array of pointers to users that are friends|
| closeFriends   |  Array   | an array of pointers to close friends|
| mentionFriends   |  Boolean   | true if the user wants to notify friends if they are mentioned in an entry, false if not|
| friendMentionNotifs   |  Boolean   | true if the user wants to be notified if friends mention them in an entry, false if not|
| mentionCloseFriends   |  Boolean   | true if the user wants to notify close friends if they are mentioned in an entry, false if not|
| closeFriendMentionNotifs   |  Boolean   | true if the user wants to be notified if close friends mention them in an entry, false if not|
| moods |  Array   | an array containing all of the user's previously selected moods|
| timeZone |  String   | The user's time zone (abbreviation) |
| currentEntry |  Pointer to Entry   | The user's current entry |


Journal entries:
| Property  |  Type   | Description |
| ----- | --- | -------- |
| objectID |  String   | unique id for the user's post |
| userID   |  Pointer to author | points to the writer of the entry |
| date   |  String   | the date a post was created / emoji was selected  |
| mood   |  String   | "Amazing", "Good", "Okay", "Bad", "Terrible", or "skip" depending on which mood the user selects|
| text   |  String   | the text of a post|
| friendMentions   |  Array   | an array of friends mentioned in the post|
| closeFriendMentions   |  Array   | an array of close friends mentioned in the post|

Mentions: 
| Property  |  Type   | Description |
| ----- | --- | -------- |
| objectID |  String   | unique id for the mention |
| fromUser   |  String | username of the author of the entry |
| toUser   |  String   | username of the user that was mentioned |
| entry   |  String   | the objectId of the entry |
| closeFriend   |  Boolean   | true if the user mentioned is a close friend, false otherwise|

### Networking

| CRUD   |  HTTP Verb   |
| ----- | --- |
| Create   |  POST   |
| Read   |  GET   |
| Update   |  PUT  |


* Home screen
    * None
* Emoji selection screen
    * (Read/GET) Query last post where user is author
    * (Update/PUT) Update the entry with the selcted mood
    * (Create/POST) Create the initial entry for the current day
* Quote screen
    * (Read/GET) Query last post where user is author as well as the last 7 entries
    * Makes network requests to [Zenquotes API](https://zenquotes.io/)
    * Makes network requests to [Dictionary API](https://dictionaryapi.dev/)
* Compose entry screen
    * (Update/PUT) Update the entry for the current day
* Calendar view of past entries screen
    * (Read/GET) Query previous posts by the user
* Scroll through past entries screen
    * (Read/GET) Query previous posts by the user
* Friends screen
   * (Read/GET) Access user's friends and close friends list
   * (Update/PUT) Update user's friends and close friends list
* View mentions screen
   * (Read/GET) Query mentions where the current user's username is in the toUser field
* View friend mentions screen
   * (Update/PUT) Delete these mentions
* View close friend mentions screen
   * none
* View mentions screen
   * (Read/GET) Query the mentions by the selected user
   * (Update/PUT) Delete these mentions


### App Walkthroughs
- [**Final App Demo**](https://drive.google.com/file/d/1B2GYKPZ9w_l2F2fhi00by5QVlVIU-8DV/view?usp=drivesdk)
- App progress
   - [Progress from week 1 days 1 and 2](https://www.dropbox.com/s/ciq8x2jyk6o8mh2/Gratitude%20Journal%20Walkthrough%201.webm?dl=0)
   - [Progress from week 1 dayday 3](https://www.dropbox.com/s/yalbvqadecqxma6/Gratitude%20Journal%20Walkthrough%202.webm?dl=0)
   - [Progress from week 1 day 4](https://www.dropbox.com/s/ar5iwr40u1lcpz2/Gratitude%20Journal%20Walkthrough%203.webm?dl=0)
   - [End of week 1 walkthrough](https://www.dropbox.com/s/uq7en7hmbip6uli/Gratitude%20Journal%20Walkthrough%204.webm?dl=0)
   - [Progress from week 2 day 1](https://www.dropbox.com/s/b1dmqp01rvfkrv2/gj%20wk2d1%20walkthrough.webm?dl=0)
   - [Progress from week 2 days 2 and 3](https://www.dropbox.com/s/ra7w5jpdajmjbek/gj%20wk2d3%20walkthrough.webm?dl=0)
   - [End of week 2 walkthrough](https://www.dropbox.com/s/y8ik68q6jxwla10/gj%20wk2%20walkthrough.webm?dl=0)
   - [Progress from week 3 day 1](https://www.dropbox.com/s/cka02otbgk5ck0d/gj%20wk3d1%20walkthrough.webm?dl=0)
   - [Progress from week 3 days 2 and 3](https://www.dropbox.com/s/6uuvrrkk56ced74/gj%20wk3d3.webm?dl=0)
   - [End of week 3 walkthrough](https://www.dropbox.com/s/cpmw1d673qeua11/gj%20wk3d4.webm?dl=0)
   - [Progress from week 4 day 1](https://www.dropbox.com/s/k5sqa8jusln9p4p/gj%20wk4d1.webm?dl=0)
   - [Progress from week 4 day 2](https://www.dropbox.com/s/e91hkme61bmhm0o/gj%20wk4d2.webm?dl=0)
   - [Progress from week 4 day 3](https://www.dropbox.com/s/u12nqds0n0ycqfr/gj%20wk4d3.webm?dl=0)
   - [Progress from week 4 day 4](https://www.dropbox.com/s/7qod6zrxw9l7vfx/gj%20wk4d4.webm?dl=0)
   - [End of week 4 walkthrough](https://www.dropbox.com/s/9b4y59pjod3pryv/gj%20wk4d5.webm?dl=0)
   - [Progress from week 5 day 3](https://www.dropbox.com/s/9f9wpsv0wnyf6ge/gj%20wk5d3.webm?dl=0)
   - [Progress from week 5 day 4](https://www.dropbox.com/s/a9x2aajxxw88498/gj%20wk5d4.webm?dl=0)
   - [End of week 5 walkthrough](https://www.dropbox.com/s/t9sfmnhsmf9c8u3/gj%20wk5d5.webm?dl=0)
