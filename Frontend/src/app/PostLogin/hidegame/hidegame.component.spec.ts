import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HidegameComponent } from './hidegame.component';

describe('HidegameComponent', () => {
  let component: HidegameComponent;
  let fixture: ComponentFixture<HidegameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HidegameComponent]
    });
    fixture = TestBed.createComponent(HidegameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
