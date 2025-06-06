import { Collection } from "./collection";

export interface User {
    id?: number;
    email: string;
    name: string;
    language: string;
    collections: Collection[];
}