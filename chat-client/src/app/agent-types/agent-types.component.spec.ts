import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentTypesComponent } from './agent-types.component';

describe('AgentTypesComponent', () => {
  let component: AgentTypesComponent;
  let fixture: ComponentFixture<AgentTypesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgentTypesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentTypesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
