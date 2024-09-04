const fs = require('fs');
const bcrypt = require('bcrypt');
const saltRounds = 10;

const firstnames = require('./constants/firstnames');
const lastnames = require('./constants/lastnames');
const cities = require('./constants/cities');
const addresses = require('./constants/addresses');
const emailDomains = require('./constants/mails');
const prefixes = require('./constants/prefixes');
const zipcodes = require('./constants/zipcode');
const comments = require('./constants/comments');
const products = require('./products.json');

function generateCode() {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let result = '';
    for (let i = 0; i < 14; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return result;
}

function formatDateToMySQL(date) {
    const d = new Date(date);
    const pad = (n) => (n < 10 ? '0' + n : n);

    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

function escapeSingleQuotes(str) {
    return str.replace(/'/g, "\\'");
}

function generateComment() {
    const comment = comments[getRandomInt(0, comments.length)];
    return escapeSingleQuotes(comment);
}

function generateSatisfaction(user_uuid, product_uuid, created_at) {
    return {
        uuid: generateCode(),
        user_uuid,
        target_uuid: product_uuid,
        target_type: "PRODUCT",
        rating: getRandomInt(1, 6),
        comment: generateComment(),
        created_at,
    };
}

function generateRandomString(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return result;
}

async function hashSHA256(message) {
    const msgBuffer = new TextEncoder().encode(message);
    const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    return hashHex;
}

async function hashPassword(password) {
    return bcrypt.hash(password, saltRounds);
}

function generatePromotionCampaign() {
    const dateStart = new Date();
    const dateEnd = new Date(dateStart);
    dateEnd.setDate(dateStart.getDate() + 7);

    return {
        type: "SUBCATEGORY",
        name: "La pause café du moment",
        discountRate: 20.0,
        targetUuid: "1b10f80b-1373-4079-bc1a-b1ebf7a982b6",
        dateStart: dateStart.toISOString().slice(0, 19),
        dateEnd: dateEnd.toISOString().slice(0, 19)
    };
}

async function generateAdminUser(products) {
    const firstname = "admin";
    const lastname = "admin";
    const email = "admin@mobalpa.fr";
    const password = await hashPassword("administrator");
    const phone_number = "0497378123";
    const birthdate = "1970-01-01";
    const address = "mobalpa";
    const zipcode = "13001";
    const city = "Marseille";
    const created_at = new Date().toISOString();
    const role_id = 3;

    const promotionCampaign = generatePromotionCampaign();

    return {
        uuid: generateCode(),
        firstname,
        lastname,
        email,
        password,
        phone_number,
        birthdate,
        address,
        zipcode,
        city,
        active: true,
        created_at,
        role_id,
        wishlist_uuid: generateCode(),
        wishlist_items: generateWishlistItems(generateCode(), products),
        payment_uuid: generateCode(),
        promotionCampaign
    };
}

async function generateUser(index, products) {
    const firstname = firstnames[getRandomInt(0, firstnames.length)];
    const lastname = lastnames[getRandomInt(0, lastnames.length)];
    const email = `${firstname.toLowerCase()}.${lastname.toLowerCase()}${index}@${emailDomains[getRandomInt(0, emailDomains.length)]}`;
    const password = await hashPassword(generateRandomString(6));
    const phone_number = `${prefixes[getRandomInt(0, prefixes.length)]}${getRandomInt(10000000, 99999999)}`;
    const birthdate = `${getRandomInt(1940, 2000)}-${getRandomInt(1, 12).toString().padStart(2, '0')}-${getRandomInt(1, 28).toString().padStart(2, '0')}`;
    const address = `${getRandomInt(1, 200)} ${addresses[getRandomInt(0, addresses.length)]}`;
    const zipcode = zipcodes[getRandomInt(0, zipcodes.length)];
    const city = cities[getRandomInt(0, cities.length)];
    const created_at = new Date().toISOString();

    let role_id;
    if (index === 1) {
        role_id = 3;
    } else if (index <= 5) {
        role_id = 2;
    } else {
        role_id = 1;
    }

    return {
        uuid: generateCode(),
        firstname,
        lastname,
        email,
        password,
        phone_number,
        birthdate,
        address,
        zipcode,
        city,
        active: true,
        created_at,
        role_id,
        wishlist_uuid: generateCode(),
        wishlist_items: generateWishlistItems(generateCode(), products),
        payment_uuid: generateCode(),
    };
}

function getRandomDateBetween(startYear, endYear) {
    const startDate = new Date(`${startYear}-01-01T00:00:00Z`).getTime();
    const endDate = new Date(`${endYear}-12-31T23:59:59Z`).getTime();
    const randomDate = new Date(startDate + Math.random() * (endDate - startDate));
    
    const pad = (n) => (n < 10 ? '0' + n : n);
    const year = randomDate.getFullYear();
    const month = pad(randomDate.getMonth() + 1);
    const day = pad(randomDate.getDate());
    const hour = pad(randomDate.getHours());
    const minute = pad(randomDate.getMinutes());
    const second = pad(randomDate.getSeconds());
    const millisecond = randomDate.getMilliseconds().toString().padStart(3, '0');
    const microsecond = Math.floor(Math.random() * 1000000).toString().padStart(6, '0');

    return `${year}-${month}-${day} ${hour}:${minute}:${second}.${millisecond}${microsecond}`;
}

function generateWishlistItems(wishlist_uuid, products) {
    const wishlistItems = [];
    const numItems = getRandomInt(8, 11);

    for (let i = 0; i < numItems; i++) {
        const product = products[getRandomInt(0, products.length)];
        const color = product.colors[getRandomInt(0, product.colors.length)];

        const wishlistItem = {
            uuid: generateCode(),
            product_uuid: product.uuid,
            quantity: 1,
            selected_color: color.name,
            wishlist_uuid: wishlist_uuid,
            brand: product.brand.name,
            imageUri: product.images[0].uri,
        };
        wishlistItems.push(wishlistItem);
    }

    return wishlistItems;
}

function createOrders(user, products) {
    const orders = [];
    for (let year = 2001; year <= 2024; year++) {
        const numOrders = getRandomInt(9, 12);
        for (let i = 0; i < numOrders; i++) {
            let total_ht = 0;
            const selectedProducts = [];
            const numProducts = getRandomInt(12, 15);

            for (let j = 0; j < numProducts; j++) {
                const product = products[getRandomInt(0, products.length)];
                const quantity = getRandomInt(1, 3);

                selectedProducts.push({
                    product_uuid: product.uuid,
                    quantity: quantity,
                    order_item_uuid: generateCode(),
                });
                total_ht += product.price * quantity;
            }

            const vat = total_ht * 0.2;
            const total_ttc = total_ht + vat;

            const order = {
                uuid: generateCode(),
                created_at: getRandomDateBetween(year, year),
                status: "PROCESSED",
                total_ht: total_ht.toFixed(2),
                vat: vat.toFixed(2),
                total_ttc: total_ttc.toFixed(2),
                products: selectedProducts
            };
            orders.push(order);
        }
    }
    return orders;
}

async function generateUsers(num, products) {
    const users = [];

    const adminUser = await generateAdminUser(products);
    users.push(adminUser);

    for (let i = 1; i <= num; i++) {
        users.push(await generateUser(i, products));
    }
    return users;
}

function generateYAML(users) {
    let yaml = 'databaseChangeLog:\n';
    users.forEach((user, index) => {
        yaml += `    - changeSet:\n`;
        yaml += `        id: ${index + 1}\n`;
        yaml += `        author: sdjfhsjb\n`;
        yaml += `        changes:\n`;
        yaml += `            - insert:\n`;
        yaml += `                tableName: "user"\n`;
        yaml += `                columns:\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "uuid"\n`;
        yaml += `                        value: "${user.uuid}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "firstname"\n`;
        yaml += `                        value: "${user.firstname}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "lastname"\n`;
        yaml += `                        value: "${user.lastname}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "email"\n`;
        yaml += `                        value: "${user.email}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "password"\n`;
        yaml += `                        value: "${user.password}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "phone_number"\n`;
        yaml += `                        value: "${user.phone_number}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "birthdate"\n`;
        yaml += `                        value: "${user.birthdate}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "address"\n`;
        yaml += `                        value: "${user.address}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "zipcode"\n`;
        yaml += `                        value: "${user.zipcode}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "city"\n`;
        yaml += `                        value: "${user.city}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "active"\n`;
        yaml += `                        valueBoolean: ${user.active}\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "created_at"\n`;
        yaml += `                        valueDate: "${user.created_at}"\n`;

        yaml += `            - insert:\n`;
        yaml += `                tableName: "user_role"\n`;
        yaml += `                columns:\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "user_uuid"\n`;
        yaml += `                        value: "${user.uuid}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "role_id"\n`;
        yaml += `                        value: "${user.role_id}"\n`;

        if (user.firstname === 'admin' && user.lastname === 'admin') {
            yaml += `            - insert:\n`;
            yaml += `                tableName: "user_role"\n`;
            yaml += `                columns:\n`;
            yaml += `                    - column:\n`;
            yaml += `                        name: "user_uuid"\n`;
            yaml += `                        value: "${user.uuid}"\n`;
            yaml += `                    - column:\n`;
            yaml += `                        name: "role_id"\n`;
            yaml += `                        value: "2"\n`;
        }

        yaml += `            - insert:\n`;
        yaml += `                tableName: "payment"\n`;
        yaml += `                columns:\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "uuid"\n`;
        yaml += `                        value: "${user.payment_uuid}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "card_holder"\n`;
        yaml += `                        value: "${user.firstname + user.lastname}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "card_number"\n`;
        yaml += `                        value: ""\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "created_at"\n`;
        yaml += `                        value: "${getRandomDateBetween(2017, 2024)}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "cvv"\n`;
        yaml += `                        value: ""\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "expiration_date"\n`;
        yaml += `                        value: "${null}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "payment_method"\n`;
        yaml += `                        value: "1"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "paypal_email"\n`;
        yaml += `                        value: "${user.email}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "user_uuid"\n`;
        yaml += `                        value: "${user.uuid}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "visitor_uuid"\n`;
        yaml += `                        value: "${null}"\n`;

        yaml += `            - insert:\n`;
        yaml += `                tableName: "wishlist"\n`;
        yaml += `                columns:\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "uuid"\n`;
        yaml += `                        value: "${user.wishlist_uuid}"\n`;
        yaml += `                    - column:\n`;
        yaml += `                        name: "user_uuid"\n`;
        yaml += `                        value: "${user.uuid}"\n`;

        user.wishlist_items.forEach((item, itemIndex) => {
            yaml += `            - sql: |\n`;
            yaml += `                INSERT INTO wishlist_item (uuid, product_uuid, quantity, selected_color, wishlist_uuid)\n`;
            yaml += `                VALUES ('${item.uuid}', UNHEX(REPLACE('${item.product_uuid}', '-', '')), ${item.quantity}, '${item.selected_color}', '${user.wishlist_uuid}');\n`;

            yaml += `                INSERT INTO wishlist_item_properties (wishlist_item_uuid, property_key, property_value)\n`;
            yaml += `                VALUES ('${item.uuid}', 'images', '${item.imageUri}');\n`;
            yaml += `                INSERT INTO wishlist_item_properties (wishlist_item_uuid, property_key, property_value)\n`;
            yaml += `                VALUES ('${item.uuid}', 'brand', '${item.brand}');\n`;

            const satisfaction = generateSatisfaction(user.uuid, item.product_uuid, user.created_at);
                yaml += `                INSERT INTO satisfaction (uuid, user_uuid, target_uuid, target_type, rating, comment, created_at)\n`;
                yaml += `                VALUES ('${satisfaction.uuid}', '${satisfaction.user_uuid}', UNHEX(REPLACE('${item.product_uuid}', '-', '')), 'PRODUCT', ${satisfaction.rating}, '${satisfaction.comment}', '${formatDateToMySQL(satisfaction.created_at)}');\n`;
        });

        const orders = createOrders(user, products);
        orders.forEach(order => {
            yaml += `            - sql: |\n`;
            yaml += `                INSERT INTO \`order\` (uuid, created_at, delivery_address, delivery_fees, delivery_method, reduction, status, total_ht, total_ttc, vat, warranty, payment_uuid, user_uuid, visitor_uuid)\n`;
            yaml += `                VALUES ('${order.uuid}', '${order.created_at}', '${user.address + user.zipcode + user.city}', '5.6', 'Chronopost', '10', '${order.status}', '${order.total_ht}', '${order.total_ttc}', '${order.vat}', NULL, '${user.payment_uuid}', '${user.uuid}', NULL);\n`;

            order.products.forEach(product => {
                yaml += `            - sql: |\n`;
                yaml += `                INSERT INTO \`order_item\` (uuid, product_uuid, quantity, order_uuid)\n`;
                yaml += `                VALUES ('${product.order_item_uuid}', UNHEX(REPLACE('${product.product_uuid}', '-', '')), '${product.quantity}', '${order.uuid}');\n`;
            });
        });

        if (user.firstname === 'admin' && user.lastname === 'admin') {
            yaml += `            - sql: |\n`;
            yaml += `                INSERT INTO campaign (type, name, discount_rate, target_uuid, date_start, date_end, created_at)\n`;
            yaml += `                VALUES ('${user.promotionCampaign.type}', '${user.promotionCampaign.name}', ${user.promotionCampaign.discountRate}, UNHEX(REPLACE('${user.promotionCampaign.targetUuid}', '-', '')), '${user.promotionCampaign.dateStart}', '${user.promotionCampaign.dateEnd}', '${user.promotionCampaign.dateStart}');\n`;
        }        
    });
    return yaml;
}

async function main() {
    const users = await generateUsers(500, products);
    const yamlOutput = generateYAML(users);
    fs.writeFileSync('createUser.yaml', yamlOutput);
    console.log('Generated db.changelog-users.yaml with 100 users.');
}

main();