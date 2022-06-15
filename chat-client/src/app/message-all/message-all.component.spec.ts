import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageAllComponent } from './message-all.component';

describe('MessageAllComponent', () => {
  let component: MessageAllComponent;
  let fixture: ComponentFixture<MessageAllComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MessageAllComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
