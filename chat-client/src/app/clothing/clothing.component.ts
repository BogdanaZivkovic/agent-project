import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { ClothingService } from '../service/clothing.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-clothing',
  templateUrl: './clothing.component.html',
  styleUrls: ['./clothing.component.css']
})
export class ClothingComponent implements OnInit {

  constructor(public clothingService : ClothingService, public userService : UserService) { }

  ngOnInit(): void {
    this.clothingService.getClothingItems(this.userService.user.username).subscribe();
  }

  sortData(sort: Sort) {
    const data = this.clothingService.clothingItems.slice();
    if (!sort.active || sort.direction === '') {
      this.clothingService.clothingItems = data;
      return;
    }
  
    this.clothingService.clothingItems = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'productName': return compare(a.productName, b.productName, isAsc);
        case 'productPrice': return compare(a.productPrice, b.productPrice, isAsc);
        case 'productDescription': return compare(a.productDescription, b.productDescription, isAsc);
        case 'productColorsNumber': return compare(a.productColorsNumber, b.productColorsNumber, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}