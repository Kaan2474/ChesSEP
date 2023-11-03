import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePlayAgainstComputerComponent } from './create-play-against-computer.component';

describe('CreatePlayAgainstComputerComponent', () => {
  let component: CreatePlayAgainstComputerComponent;
  let fixture: ComponentFixture<CreatePlayAgainstComputerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreatePlayAgainstComputerComponent]
    });
    fixture = TestBed.createComponent(CreatePlayAgainstComputerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
