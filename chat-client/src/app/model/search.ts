export class Search {
    constructor(
        public productName: string,
        public productDescription: string,
        public minPrice: number | null,
        public maxPrice: number | null,
        public minColorNumber: number | null,
        public maxColorNumber: number | null,
    ) {}
}