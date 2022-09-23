'use strict';

const { Contract } = require('fabric-contract-api');

class Col4IndLog extends Contract {

    async initLedger(ctx) {
        console.info('============= START : Initialize Ledger 2 ===========');
        const documents = [
        ];
        for (let i = 0; i < documents.length; i++) {
            await ctx.stub.putState('DOCUMENT' + i, Buffer.from(JSON.stringify(documents[i])));
            console.info('Added <--> ', documents[i]);
        }
        console.info('============= END : Initialize Ledger ===========');
    }

    async uploadDocument(ctx, document) {
        console.info('============= START : Upload Document ===========');
        const json = JSON.parse(document);
        await ctx.stub.putState(json['DocumentUniqueIdentifier'], Buffer.from(JSON.stringify(json)));

        console.info('============= END : Upload Document ===========');
    }

    async alterDocument(ctx, documentNumber, field, value) {
        console.info('============= START : alterDocument ===========');

        const documentAsBytes = await ctx.stub.getState(documentNumber);
        if (!documentAsBytes || documentAsBytes.length === 0) {
            throw new Error(`${documentNumber} does not exist`);
        }
        const document = JSON.parse(documentAsBytes.toString());
        document[field] = value;

        await ctx.stub.putState(documentNumber, Buffer.from(JSON.stringify(document)));
        console.info('============= END : alterDocument ===========');
    }

    async getHistory(ctx, key) {
        console.info('============= START : getHistory ===========', key);

        let iterator = await ctx.stub.getHistoryForKey(key);
        let allResults = [];
        let res = await iterator.next();

        while (!res.done) {
            let jsonRes = {};
           
            try {
                jsonRes = JSON.parse(res.value.value.toString('utf8'));
                delete jsonRes['DocumentChecksum'];
                delete jsonRes['DocumentFile'];
            } catch (err) {
                // console.log(err);
                jsonRes = res.value.value.toString('utf8');
            }
            allResults.push(jsonRes);
            res = await iterator.next();
        }
        iterator.close();

        console.info('============= END : getHistory ===========');

        return JSON.stringify(allResults)

    }
    
    async downloadDocumentVersion(ctx, key, version) {
        console.info('============= START : downloadDocumentVersion ===========', key);

        let iterator = await ctx.stub.getHistoryForKey(key);
        let res = await iterator.next();
        let document = {};
        while (!res.done) {
            let jsonRes = {};
           
            try {
                jsonRes = JSON.parse(res.value.value.toString('utf8'));
                if(jsonRes['Version'] == version) {
                    document = jsonRes;
                }
            } catch (err) {
                // console.log(err);
                jsonRes = res.value.value.toString('utf8');
                
                if(jsonRes['Version'] == version) {
                    document = jsonRes['DocumentFile'];
                }
            }
            res = await iterator.next();
        }
        iterator.close();

        console.info('============= END : downloadDocumentVersion ===========');

        return JSON.stringify(document);

    }

    async downloadDocument(ctx, documentNumber) {
        const documentAsBytes = await ctx.stub.getState(documentNumber);
        
        if (!documentAsBytes || documentAsBytes.length === 0) {
            throw new Error(`${documentNumber} does not exist`);
        }
        // console.log(documentAsBytes.toString());
        return documentAsBytes.toString();
    }

    async checkDocument(ctx, queryString) {
        console.info('============= START : checkDocument ===========');

        let iterator = await ctx.stub.getQueryResult(queryString);
        let allResults = [];
        let res = await iterator.next();
        while (!res.done) {
            if (res.value && !res.value.key.includes('initialized') && res.value.value.toString()) {
                let jsonRes = {};
                // console.log(res.value.value.toString('utf8'));
                jsonRes.Key = res.value.key;
                try {
                    jsonRes = JSON.parse(res.value.value.toString('utf8'));
                } catch (err) {
                    console.log(err);
                    jsonRes = res.value.value.toString('utf8');
                }
                allResults.push(jsonRes);
            }
            res = await iterator.next();
        }
        iterator.close();
        console.info('============= END : checkDocument ===========');
        return JSON.stringify(allResults);
    }

    async queryDocuments(ctx, queryString) {
        console.info('============= START : Query ===========');

        let iterator = await ctx.stub.getQueryResult(queryString);
        let allResults = [];
        let res = await iterator.next();
        while (!res.done) {
            if (res.value && !res.value.key.includes('initialized') && res.value.value.toString()) {
                let jsonRes = {};
                // console.log(res.value.value.toString('utf8'));
                jsonRes.Key = res.value.key;
                try {
                    jsonRes = JSON.parse(res.value.value.toString('utf8'));
                    delete jsonRes['DocumentChecksum'];
                    delete jsonRes['DocumentFile'];
                } catch (err) {
                    console.log(err);
                    jsonRes = res.value.value.toString('utf8');
                }
                allResults.push(jsonRes);
            }
            res = await iterator.next();
        }
        iterator.close();
        console.info('============= END : Query ===========');

        return JSON.stringify(allResults);
    }

}

module.exports = Col4IndLog;
