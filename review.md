# Review — Concurrent TCP Chat

## General Feedback

Nice work on this concurrent TCP chat. You’ve got a clear Client/Server Maven split, a dedicated receive thread on the client so send and receive can both block without stepping on each other, and a server that accepts multiple connections and broadcasts cleanly. Username on join, `/quit` (plus a few friendly aliases), and the optional extras (`/list`, `/whisper`, `/name`, `/help`) are all in place — the chat feels complete and usable. Packaging with `maven-jar-plugin` is set up the right way for executable JARs. 

## Possible improvements and considerations

- Clients are added to `ClientManager` before the username is chosen. That can allow two people to grab the same name at the same time, and `/list` may briefly show `null`. Adding the client only after a successful name (or locking check + set + add together) would tighten this up.
- If a username is already taken, the server closes the connection but the client send loop may keep waiting on stdin — a clearer exit or re-prompt on the client would help.
- Some naming (`FtpCommand`, `ROOT_FOLDER`, control-port style constants) is leftover from the previous FTP assignment; cleaning that up would make the chat project easier to follow.



Cool stuff! Keep it up!