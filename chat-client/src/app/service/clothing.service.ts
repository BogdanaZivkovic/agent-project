import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClothingItem } from '../model/clothingItem';
import { Search } from '../model/search';

const baseUrl = 'http://localhost:8080/Chat-war/api/webScrape/';

@Injectable({
  providedIn: 'root'
})
export class ClothingService {

  clothingItems: ClothingItem[] = [];

  constructor(private http : HttpClient) { }

  getClothingItems(username : string, search: Search) {
    return this.http.post(baseUrl + "clothingItems/" + username, search);
  }
}
