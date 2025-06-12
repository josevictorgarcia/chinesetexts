import { Collection } from "./collection";

export interface UserWithPassword {
    id?: number;
    email: string;
    name: string;
    password: string;
    newPassword?: string;
    language: string;
    collections: Collection[];
    roles: string[];
}