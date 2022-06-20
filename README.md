Original App Design Project - README Template
===

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

* User can create a new account
* User can log in
* User can log out
* User can select an emoji that matches their mood for the day
    * There is a skip option
* User can view a quote that is generated based on the emoji mood
    * Quote is randomized if the user skips the emoji selection page
* User can write an one entry each day and edit that entry at any time
* User can view a slideshow/scroll through all entries and see the date each was created
* User can view a calendar, which they can click on to see the entries from each day.
    * selecting a week and doing a 2 finger zoom in expands that week rather than displaying the entire month

**Optional Nice-to-have Stories**

* User can add images into their journal entry
    * The location of the images can be displayed in the entry (taken from google photos?)
    * User can add a background image to their entry
* User can change the background color of their entries
* User can send friend requests to other users
* User receives a notification when a friend mentions them in their gratitude journal entry (or the full post send as disappearing, non screenshottable messages, depending on the user's preference (close friends vs just friends))
    * User can turn on/off sending these notifications
    * User can turn on/off receiving these notifications
* User receives push notifications near the end of the day if they have not written an entry for the day
    * In the form of a word cloud
* User can see stats based on which word / group of words they use the most in their journal entries
    * Most common words in general
    * Most common words for each mood
* Composed entries are auto-saved even if the user does not select the save button
* Days on calendar are colored based on emoji selected
* While the user writes their entry, the app plays music based on their emoji mood selection
    * Music is randomized if the user skips the emoji selection page
    * User can turn this off in the app's settings

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
* View past entries screen
    * User can select slideshow view or calendar view
* Slideshow view of past entries screen
    * User can scroll through past entries and see the emoji mood they selected that day
* Calendar view of past entries screen
    * User can view a calendar, which they can click on to see the entries from each day.
    * selecting a week and doing a 2 finger zoom in expands that week rather than displaying the entire month
    * Days on calendar are colored based on emoji selected (if stretch goal is acheived)
* Past entry screen
    * Displays one past entry and the associated emoji

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
    * --> view past entries screen
* Emoji selection screen
    * --> home screen
    * automatically transitions to quote screen
* Quote screen
    * --> home screen
    * "next" button transitions to compose entry screen
* Compose entry screen
    * --> home screen (tab)
* View past entries screen
    * --> home screen (tab)
    * --> slideshow view screen
    * --> calendar view screen
* Slideshow view of past entries screen
    * --> past entry screen
* Calendar view of past entries screen
    * --> past entry screen

## Wireframes
![alt text](https://github.com/anikapatel999/Gratitude-Journal/blob/main/Wireframes.jpeg)

### [BONUS] Digital Wireframes & Mockups

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
| text   |  String   | the text of a post       |
| image1   |  File   | the first image a user adds to their post|
| image2   |  File   | the second image a user adds to their post|
| image3   |  File   | the third image a user adds to their post|
| image4   |  File   | the fourth image a user adds to their post|
| image5   |  File   | the fifth image a user adds to their post|
| friendMentions   |  Array   | an array of friends mentioned in the post|
| closeFriendMentions   |  Array   | an array of close friends mentioned in the post|


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
