import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoingameComponent } from './joingame.component';

describe('JoingameComponent', () => {
  let component: JoingameComponent;
  let fixture: ComponentFixture<JoingameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JoingameComponent]
    });
    fixture = TestBed.createComponent(JoingameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
