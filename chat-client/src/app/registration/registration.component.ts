import { Component, OnInit } from '@angular/core';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  model = new User('', '');

  constructor(private userService : UserService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.userService.register(this.model);
    console.log(this.model);
  }
}
