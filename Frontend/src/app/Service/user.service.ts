import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../Modules/User";


@Injectable({
  providedIn: "root"
})
export class UserService {
  private userURL: string;
  token = localStorage.getItem("JWT")

  header = new HttpHeaders().set("Authorization", "Bearer " + this.token)
    .set("Access-Control-Allow-Origin", "*")
    .set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
    .set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")


  constructor(private http: HttpClient,
  ) {
    this.userURL = "http://localhost:8080/users";

  }

  public register(user: User) {
    return this.http.post(this.userURL + "/register", user);
  }

  public login(user: User) {
    return this.http.post<User>(this.userURL + "/authenticate", user);
  }

  public checkCode(user: User) {
    return this.http.post<User>(this.userURL + "/twoFactor", user);
  }

  public getUser(user: User) {
    return this.http.get<User>(this.userURL + "/{userId}/" + user.id ,{headers:this.header} );

  }



  public getProfil(user:User){
    return this.http.get(this.userURL + "/myProfile" ,{headers:this.header} );
  }

}

