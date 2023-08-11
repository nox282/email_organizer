const fs = require('fs');
const path = require('path');

const sourceJsFolder = path.join(__dirname, '..', 'node_modules', 'bootstrap', 'dist', 'js');
const sourceCssFolder = path.join(__dirname, '..', 'node_modules', 'bootstrap', 'dist', 'css');
const destinationFolder = path.join(__dirname, '..', 'assets');

const jsAssetsToCopy = [
  'bootstrap.bundle.min.js'
];

const cssAssetsToCopy = [
  'bootstrap.min.css'
];

const copyAsset = (sourceFolder, asset, destinationPath) => {
  const sourcePath = path.join(sourceFolder, asset);
  
  fs.copyFile(sourcePath, destinationPath, err => {
    if (err) {
      console.error(`Error copying ${asset}: ${err}`);
    } else {
      console.log(`${asset} copied successfully.`);
    }
  });
};

jsAssetsToCopy.forEach(asset => {
  const destinationPath = path.join(destinationFolder, asset);
  copyAsset(sourceJsFolder, asset, destinationPath);
});

cssAssetsToCopy.forEach(asset => {
  const destinationPath = path.join(destinationFolder, asset);
  copyAsset(sourceCssFolder, asset, destinationPath);
});
