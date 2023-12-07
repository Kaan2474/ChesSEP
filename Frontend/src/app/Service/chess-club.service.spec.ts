import { TestBed } from '@angular/core/testing';

import { ChessClubService } from './chess-club.service';

describe('ChessClubService', () => {
  let service: ChessClubService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChessClubService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
