import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePlayAgainstUserComponent } from './create-play-against-user.component';

describe('CreatePlayAgainstUserComponent', () => {
  let component: CreatePlayAgainstUserComponent;
  let fixture: ComponentFixture<CreatePlayAgainstUserComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreatePlayAgainstUserComponent]
    });
    fixture = TestBed.createComponent(CreatePlayAgainstUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
