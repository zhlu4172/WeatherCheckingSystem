# Major Project
<br/>

### Input API: Weatherbit
### Output API: Pastebin
### Claimed Tier: Distinction
### Credit Optional Feature 1: About
### Credit Optional Feature 2: Screenshot
### Distinction Optional Feature: Spinning progress Indicator
### High Distinction Optional Feature: 
<br/>

### Milestone 1 Commit
    SHA: f76f6f3c1d6687d9b5bc51ee7cb56bdecbb86406
    URI: https://github.sydney.edu.au/zhlu4172/SCD2_2022/tree/f76f6f3c1d6687d9b5bc51ee7cb56bdecbb86406


### Milestone 1 Resubmission Commit
    SHA: f76f6f3c1d6687d9b5bc51ee7cb56bdecbb86406
    URI: https://github.sydney.edu.au/zhlu4172/SCD2_2022/tree/f76f6f3c1d6687d9b5bc51ee7cb56bdecbb86406


### Milestone 2 Commit
    SHA: 1a92cca62af360802f5849a2b1fb0351e4e7cc17
    URI: https://github.sydney.edu.au/zhlu4172/SCD2_2022/tree/1a92cca62af360802f5849a2b1fb0351e4e7cc17

### Milestone 2 Resubmission Commit
    SHA: b7e642940e00df20487415d63801c516b54e7dd5
    URI: https://github.sydney.edu.au/zhlu4172/SCD2_2022/tree/b7e642940e00df20487415d63801c516b54e7dd5

### Exam Base Commit:
    SHA: b7e642940e00df20487415d63801c516b54e7dd5
    URI: https://github.sydney.edu.au/zhlu4172/SCD2_2022/tree/b7e642940e00df20487415d63801c516b54e7dd5

### Exam Submission Commit:
    SHA: 02aa45e5ab25b1cd73501cefd2aa55b58a504904
    URI: https://github.sydney.edu.au/zhlu4172/SCD2_2022/tree/02aa45e5ab25b1cd73501cefd2aa55b58a504904

# Instructions
<br/>

## Preparation

By using this app, you should first set 2 environmental variables in your local machine.
<br/>

#### INPUT_API_KEY
#### PASTEBIN_API_KEY

You can set that by typing 
<br/>
Windows:
<br/>
$env:INPUT_API_KEY="Your key here"
<br/>
$env:PASTEBIN_API_KEY="Your developer key here"
<br/>
Macbook:
<br/>
export INPUT_API_KEY="Your key here"
<br/>
export PASTEBIN_API_KEY="Your developer key here"

## Introduction
This application is used to search weather in different cities around the world.

### Version
<br/>
There are two versions for input and output each.
<br/>
You can decide on the version you want to use by using the following commands when you start the application.
<br/>

#### gradle run
<br/> By using this to start up this command, input and output will all be set the the default version: online version
<br/>

#### gradle run --args="online online"
<br/> By using this to start up this command, input and output will all be set to the online version.
<br/>

#### gradle run --args="offline offline"
<br/> By using this to start up this command, input and output will all be set to the offline version
<br/>

#### gradle run --args="online offline"
<br/> By using this to start up this command, input will be set to the online version and output will be set to the offline version.
<br/>

#### gradle run --args="offline online"
<br/> By using this to start up this command, input will be set to the offline version and output will be set to the online version.
<br/>

## Main Page

A world map is shown on the left top of the window.

### Search the city weather<Hurdle/Pass
<br/>
An autocomplete combo box is on the left bottom, you can type in the city name you want to search.

<br/>
Then you just need to select the city you want the search weather and click on the confirm button. Then you can see that a weather icon will be shown on the world map and a searching history will be in the right top of the window, under the 'Search history'. 
<br/> 
You can choose the one history you want to delete on the right top list view by just clicking on that and then click on the delete button. Also, you can clear all that you searched by clicking on the clear button.
<br/>
Weather details will be shown in the right bottom of the window.
If some data missing, it will show: No data provided. If there is not any data for the city you searched, an alert will pop up indicating that there's no data about that city.
<br/>

<br/>


### Send report <Hurdle/Pass
<br/>
By clicking on the send report button on top right, you can paste all the search history to an external website. A response window will pop up indicating whether your paste is successful and a paste url will be provided for you, you can click on the copy url button and open in an external web browser.
<br/>

### Cache <CR
<br/>
For each search, the searching history will be recorded to cache, so next time if you search for the same city, a window will pop up asking you to use cache to create a new search. If you choose to use the cache, the app will use the searching history and will not make a fresh request. If you choose to make a fresh one, the app will make a new request.
<br/>


### Screenshot/Snapshot <CR
<br/>
By clicking on the snapshot button on right bottom, you could get the screenshot of this app and save to any place on your local machine. png is the default picture type.
<br/>

### About <CR
<br/>
There is a menubar on the top left and you can click on that then choose about menu item. Then an about window will pop up.
<br/>


### Concurrency <DI
<br/>
Each time you create an api call and wait for response, the GUI will not be frozen. You can also click on other buttons while waiting the api response.
<br/>

### Spinning progress <DI
<br/>
Each time you start an api fetch (clicking on the confirm or send report button), the spinning progress will starts to roll and after you get the response from the external webstie, it will show a 'done'.
<br/>

# References

Input API: https://www.weatherbit.io/api

Output API: https://pastebin.com/doc_api

referencing and modifying autocompletebox code from: https://www.techgalery.com/2019/08/javafx-combo-box-with-search.html
