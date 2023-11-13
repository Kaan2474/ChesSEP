import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayGameAgainstUserComponent } from './play-game-against-user.component';

describe('PlayGameAgainstUserComponent', () => {
  let component: PlayGameAgainstUserComponent;
  let fixture: ComponentFixture<PlayGameAgainstUserComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayGameAgainstUserComponent]
    });
    fixture = TestBed.createComponent(PlayGameAgainstUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
