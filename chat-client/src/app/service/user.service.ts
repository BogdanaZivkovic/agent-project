import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Message } from '../model/message';
import { User } from '../model/user';
import { MessageService } from './message.service';

const baseUrl = 'http://localhost:8080/Chat-war/api/users/';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  isSignedIn = false;
  loggedInUsers: User[] = [];
  registeredUsers: User[] = [];
  user: User = new User('', '');

  constructor(private http : HttpClient, private router: Router, private messageService : MessageService) { }

  signIn(user : User ) {
    return this.http.post(baseUrl + "login", user).subscribe({
      next: (data) => {
        this.user = data as User;
        this.isSignedIn = true;
        initSocket(this, this.router, this.messageService);
      },
      error: () => {alert("Error: Please enter correct username and password!")}
    })
  }
  
  signOut() {
    return this.http.delete(baseUrl + "loggedIn/" + this.user.username).subscribe({
      next: () => {
        this.isSignedIn = false;
        this.user = new User('', '');
      }
    })
  }

  register(user: User) {
    return this.http.post(baseUrl + "register", user).subscribe({
      next: () => {alert("Successfully registered!")},
      error: () => {alert("Error: User with this username already exists!")}
    });
  }

  getLoggedInUsers() {
    return this.http.get(baseUrl + "loggedIn").subscribe()
  }

  getRegisteredUsers() {
    return this.http.get(baseUrl + "registered").subscribe()
  }
}

function initSocket(userService: UserService, router: Router, messageService : MessageService) {
  let connection: WebSocket|null = new WebSocket("ws://localhost:8080/Chat-war/ws/" + userService.user.username); 

  connection.onopen = function () {
    console.log("Socket is open");
  }

  connection.onclose = function () {
    connection = null;
  }

  connection.onmessage = function (msg) {
    const data = msg.data.split("!");

    if(data[0] == "LOGGEDIN") {
      let users: User[] = [];
      data[1].split("|").forEach((user: string) => {
        if (user) {
          let userData = user.split(",");   
          users.push(new User(userData[0], userData[1]))
        }
     });    
     userService.loggedInUsers = users;   
    }
    else if(data[0] == "REGISTERED") {
      let users: User[] = [];
      data[1].split("|").forEach((user: string) => {
        if (user) {
          let userData = user.split(",");   
          users.push(new User(userData[0], userData[1]))
        }
     });    
     userService.registeredUsers = users;   
    }
    else if(data[0] == "MESSAGES") {
      let messages: Message[] = [];
      data[1].split("|").forEach((message: string) => {
        if (message) {
          let messageData = message.split(",");   
          messages.push(new Message(userService.user, new User(messageData[0], ""), new Date(messageData[1]), messageData[2], messageData[3]))
        }
     });    
     messageService.messages = messages;   
    }
    else if(data[0] == "MESSAGE") {
      let messageData = data[1].split(",");   
      messageService.messages.push(new Message(userService.user, new User(messageData[0], ""), new Date(messageData[1]), messageData[2], messageData[3]));
      alert(data[1]);
    }
    else {
      alert(data[1]);
    }
  }
}

