import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignedInUsersComponent } from './signed-in-users.component';

describe('SignedInUsersComponent', () => {
  let component: SignedInUsersComponent;
  let fixture: ComponentFixture<SignedInUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SignedInUsersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SignedInUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
