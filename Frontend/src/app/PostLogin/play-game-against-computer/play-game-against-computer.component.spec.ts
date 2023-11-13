import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayGameAgainstComputerComponent } from './play-game-against-computer.component';

describe('PlayGameAgainstComputerComponent', () => {
  let component: PlayGameAgainstComputerComponent;
  let fixture: ComponentFixture<PlayGameAgainstComputerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayGameAgainstComputerComponent]
    });
    fixture = TestBed.createComponent(PlayGameAgainstComputerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
