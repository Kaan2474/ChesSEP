import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessPuzzleComponent } from './chess-puzzle.component';

describe('ChessPuzzleComponent', () => {
  let component: ChessPuzzleComponent;
  let fixture: ComponentFixture<ChessPuzzleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessPuzzleComponent]
    });
    fixture = TestBed.createComponent(ChessPuzzleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
