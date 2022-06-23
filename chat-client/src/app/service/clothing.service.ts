import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClothingItem } from '../model/clothingItem';

const baseUrl = 'http://localhost:8080/Chat-war/api/webScrape/';

@Injectable({
  providedIn: 'root'
})
export class ClothingService {

  clothingItems: ClothingItem[] = [];

  constructor(private http : HttpClient) { }

  getClothingItems(username : string) {
    return this.http.get(baseUrl + "clothingItems/" + username);
  }
}
