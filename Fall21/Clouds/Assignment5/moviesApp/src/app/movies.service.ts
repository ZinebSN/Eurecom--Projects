import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import firebase from 'firebase/compat/app';
import { User } from './user.model';
import { Router } from '@angular/router';
import { AngularFirestore } from '@angular/fire/compat/firestore';
import { Movie } from './movie.model';
import { AngularFireDatabase } from '@angular/fire/compat/database';

@Injectable({
  providedIn: 'root'
})
export class MoviesService {
  private user: User ;
 
  
  constructor(private afAuth: AngularFireAuth, private router: Router, private firestore:AngularFirestore, private realtimedb:AngularFireDatabase) { }

  async signInWithGoogle() {
    const credientals= await this.afAuth.signInWithPopup(new firebase.auth.GoogleAuthProvider)
    
    this.user= {
      
      uid: credientals.user.uid,
      
      displayName:credientals.user.displayName,
      
      email:credientals.user.email
    };

    localStorage.setItem("user", JSON.stringify(this.user));
    this.updateUserData();
    this.router.navigate(["dashboard"]);
  }

  private updateUserData() {
    this.firestore.collection("users").doc(this.user.uid).set({
      uid:this.user.uid,
      displayName: this.user.displayName,
      email: this.user.email
    }, {merge: true});
    
  }
 
  getUser(){
    
    if (this.user==null && this.userSignedIn()){
      
      this.user=JSON.parse(localStorage.getItem("user"));
    }
   
    return this.user;
    
  }

  userSignedIn(): boolean{     
    return JSON.parse(localStorage.getItem('user')) != null;
    
  }

  logOut(){

    this.afAuth.signOut();
    localStorage.removeItem("user");
    this.user=null;
    this.router.navigate(["signin"]);
  }
  
  goToDashboard(){
    this.router.navigate(["dashboard"]);
  }
  goToWishlist(){
    this.router.navigate(["wishlist"]);
  }

  getMovies(): Observable<any> {
    return this.realtimedb.list<Movie>('movies-list').valueChanges(); 
  }

  getWishlist(): Observable<any>{
    return this.firestore.collection("users").doc(this.user.uid).collection<Movie>("wishlist").valueChanges();
  }


  addMovie(movie:Movie){
    let id:string=movie.id.toString();
    this.firestore.collection("users").doc(this.user.uid).collection("wishlist").doc(id).set(movie);
  }

  removeMovie(movie:Movie){
    this.firestore.collection("users").doc(this.user.uid).collection<Movie>("wishlist").doc(movie.id.toString()).delete();
  }
}
