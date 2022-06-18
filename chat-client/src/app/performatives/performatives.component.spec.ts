import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformativesComponent } from './performatives.component';

describe('PerformativesComponent', () => {
  let component: PerformativesComponent;
  let fixture: ComponentFixture<PerformativesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PerformativesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformativesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
