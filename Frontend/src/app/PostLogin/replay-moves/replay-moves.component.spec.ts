import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReplayMovesComponent } from './replay-moves.component';

describe('ReplayMovesComponent', () => {
  let component: ReplayMovesComponent;
  let fixture: ComponentFixture<ReplayMovesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReplayMovesComponent]
    });
    fixture = TestBed.createComponent(ReplayMovesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
