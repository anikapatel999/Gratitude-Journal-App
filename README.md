Gratitude Journal App
===

For a more updated version that also contains my notes, please see [this HackMD document](https://hackmd.io/@anikapatel/SkGgjlWFq)

# Gratitude Journal

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app allows users to make an entry in their gratitude journal each day, as well as select a mood for each day and view past entries.

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

* [ ] User can create a new account ***(3 hrs)***
* [ ] User can log in ***(3 hrs)***
* [ ] User can log out ***(1 hr)***
* [ ] User can select an emoji that matches their mood for the day ***(1 day)***
    *  [ ] There is a skip option
        * *instead, maybe they can swipe to the next screen?*
            * *this probably counts as a required gesture? if so, it can replace the zoom option on the calendar, but I should double check*
    * Consider using a slider "how was your day?" terrible-->amazing instead
* [ ] User can write an one entry each day and edit that entry at any time during that day ***(2 days)***
* [ ] User can view a quote that is generated based on the emoji mood ***(3 days)***
    * [Algorithm walkthrough (not pseudocode yet)](https://hackmd.io/@anikapatel/H1D-74Ktq/edit)
    * user can swipe to the compose activity (same as from the emoji activity to the quote activity)
* [ ] User can view a calendar, which they can click on to see the entry from that day and scroll through the entries both forward and backwards ***(4 days)***.
    * [ ] implements infinite scroll (this will probably take most of the time)
    * [ ] Would be made much easier by finding a calendar widget
    * ~~selecting a week and doing a 2 finger zoom in expands that week rather than displaying the entire month~~
        * *this seems kind of pointless*
* [ ] The UI is nice ***(3 days)***
    * [ ] Use this library (https://lottiefiles.com/) to make pretty buttons
    * [ ] Buttons change when they're clicked on (maybe use the same gif but change the color palettes?)

**Optional Nice-to-have Stories (In order of priority)**
* [ ] User can send friend requests to other users ***(1 day)***
* [ ] User receives a notification when a friend mentions them in their gratitude journal entry (or the full post send as disappearing, non screenshottable messages, depending on the user's preference (close friends vs just friends)) ***(4 days)***
    * [Overview of how this works (with a couple of examples)](https://hackmd.io/@anikapatel/HyQaiVKF5/edit)
    *  [ ] User can turn on/off sending these notifications
    *  [ ] User can turn on/off receiving these notifications
* [ ] User receives push notifications near the end of the day if they have not written an entry for the day ***(1 day)***
* [ ] User can see stats based on which word / group of words they use the most in their journal entries ***(3 days)***
    *  [ ] Most common words in general
    *  [ ] Most common words for each mood
    *  [ ] Words this week
    * In the form of a word cloud (there's an [API](https://wordcloudapi.com/) for that)
* [ ] Composed entries are auto-saved even if the user does not select the save button ***(3 hrs)***
* [ ] User can add images into their journal entry ***(2 days)***
    *  [ ] User can add a background image to their entry
    *  [ ] The images can be placed anywhere in the entry
* [ ] User can change the background color of their entries ***(3 hrs)***
    * not necessary if previous goal is fulfilled
* [ ] Days on calendar are colored based on emoji selected ***(3 hrs)***
* [ ] While the user writes their entry, the app plays music based on their emoji mood selection ***(3 days)***
    *  [ ] Music is randomized if the user skips the emoji selection page
    *  [ ] User can turn this off in the app's settings

### 2. Screen Archetypes

* Registration screen
    * User can create an account
* Login screen
    * User can log in
* Home screen
    * User can select from 3 buttons:
        * Compose/edit entry
        * View calendar
        * View/edit friends list (for a stretch goal)
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
    * ~~selecting a week and doing a 2 finger zoom in expands that week rather than displaying the entire month~~
    * Days on calendar are colored based on emoji selected (if stretch goal is acheived)
* Scroll view of past entries screen
    * User can scroll through entries and see the emoji mood they selected that day

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* User can navigate to the home screen from anywhere
* * User can log out from anywhere

**Flow Navigation** (Screen to Screen)

* Registration screen
    * --> home screen
* Login screen
    * --> home screen
* Home screen
    * --> compose entry screen
    * --> Calendar view of past entries screen
* Emoji selection screen
    * --> home screen
    * automatically transitions to quote screen
* Quote screen
    * --> home screen
    * "next" button transitions to compose entry screen
* Compose entry screen
    * --> home screen (tab)
* Calendar view of past entries screen
    * --> home screen (tab)
    * --> slideshow view screen
    * --> calendar view screen
* Scroll view of past entries screen
    * none

## Wireframes!
![](https://i.imgur.com/HAANadd.jpg)


### [BONUS] Digital Wireframes & Mockups![]


### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
**Parse:**

Users:
| Property  |  Type   | Description |
| ----- | --- | -------- |
| objectID |  String   | unique id for the user |
| name   |  Pointer to author | name of the user |
| username   |  String   | the user's username  |
| password   |  String   |the user's password|
| createdAt   |  String   | the date the user joined the app |
| friends  |  Array   | an array of pointers to users that are friends|
| closeFriends   |  Array   | an array of pointers to close friends|
| mentionFriends   |  Boolean   | true if the user wants to notify friends if they are mentioned in an entry, false if not|
| friendMentionNotifs   |  Boolean   | true if the user wants to be notified if friends mention them in an entry, false if not|
| mentionCloseFriends   |  Boolean   | true if the user wants to notify close friends if they are mentioned in an entry, false if not|
| closeFriendMentionNotifs   |  Boolean   | true if the user wants to be notified if close friends mention them in an entry, false if not|


Journal entries:
| Property  |  Type   | Description |
| ----- | --- | -------- |
| objectID |  String   | unique id for the user's post |
| userID   |  Pointer to author | points to the writer of the entry |
| date   |  String   | the date a post was created / emoji was selected  |
| emoji   |  Number   | 0-4 depending on the user's happiness|
| text   |  String   | the text of a post|
| image1   |  File   | the first image a user adds to their post|
| image2   |  File   | the second image a user adds to their post|
| image3   |  File   | the third image a user adds to their post|
| image4   |  File   | the fourth image a user adds to their post|
| image5   |  File   | the fifth image a user adds to their post|
| friendMentions   |  Array   | an array of friends mentioned in the post|
| closeFriendMentions   |  Array   | an array of close friends mentioned in the post|
| visFriendMentions   |  Array   | an array of pointers to entries that the user has been mentioned in as a friend. These elements are deleted as they are accessed|
| visCloseFriendMentions   |  Array   | an array of pointers to entries that the user has been mentioned in as a close friend. These elements are deleted as they are accessed|
| mentions |  Array   | an array containing all friends/close friends who have mentioned the user (this will only be used if I implement the user stats stretch feature)|

### Networking
- [Add list of network requests by screen ]


*These only include Parse requests (for now)*
| CRUD   |  HTTP Verb   |
| ----- | --- |
| Create   |  POST   |
| Read   |  GET   |
| Update   |  PUT  |


* Home screen
    * None
* Emoji selection screen
    * (Read/GET) Query last post where user is author
* Quote screen
    * None
* Compose entry screen
    * (Create/POST) Create the initial entry for the current day
    * (Update/PUT) Update the entry for the current day
* View past entries screen
    * None
* Slideshow view of past entries screen
    * (Read/GET) Query previous posts where user is author
* Calendar view of past entries screen
    * None
* Past entry screen
    * (Read/GET) Query previous post on correct day where user is author
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
